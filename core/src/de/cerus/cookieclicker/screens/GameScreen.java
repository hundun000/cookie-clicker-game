/*
 *  Copyright (c) 2018 Cerus
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * Cerus
 *
 */

package de.cerus.cookieclicker.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.*;
import de.cerus.cookieclicker.CookieClickerGame;
import de.cerus.cookieclicker.data.Data;
import de.cerus.cookieclicker.fixes.CustomShapeRenderer;
import de.cerus.cookieclicker.objects.MiniCookie;
import de.cerus.cookieclicker.objects.Shop;
import de.cerus.cookieclicker.util.FontUtil;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameScreen implements Screen {

    private static float COOKIE_WIDTH = 200;
    private static float COOKIE_HEIGHT = 200;
    private static float COOKIE_MAX_WIDTH = 200;
    private static float COOKIE_MAX_HEIGHT = 200;

    private CustomShapeRenderer shapeRenderer;

    private CookieClickerGame game;
    private OrthographicCamera camera;
    private Shop shop;
    private DecimalFormat format;

    private Texture cookieTexture;
    private Texture shopTexture;
    private Texture clickerTexture;

    private Ellipse cookieRepresentation;
    private Rectangle shopRepresentation;

    private double cookiesPerSecond;
    private int clickerAnimationIndex;

    private Queue<MiniCookie> cookies = new ConcurrentLinkedQueue<>();
    private int amountMiniCookies;
    private static int MINICOOKIE_WIDTH = 25;
    private static int MINICOOKIE_HEIGHT = 25;
    private static float MINICOOKIE_THRESHOLD = -1;
    private static float MINICOOKIE_SPEED = 0.8f;
    private static float MINICOOKIE_ROTATION_SPEED = 0.25f;

    private float generalRotation = 0;

    public GameScreen(CookieClickerGame game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        this.shop = new Shop();
        this.format = new DecimalFormat("#,###");

        this.cookieTexture = new Texture(Gdx.files.internal("cookie.png"));
        this.shopTexture = new Texture(Gdx.files.internal("shop.png"));
        this.clickerTexture = new Texture(Gdx.files.internal("pointer.png"));

        this.cookieRepresentation = new Ellipse();
        this.shopRepresentation = new Rectangle();

        this.shapeRenderer = new CustomShapeRenderer();
    }

    @Override
    public void show() {
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        loadDataForShop();

        clickerAnimationIndex = -1;

        ScheduledExecutorService service = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() / 4);
        service.scheduleAtFixedRate(new Runnable() {
            private int index = 0;
            private long lastCookies = 0;

            @Override
            public void run() {
                cookiesPerSecond = shop.getCookies() - lastCookies;
                lastCookies = shop.getCookies();

                if (index >= Shop.MAX_CLICKER) {
                    index = 0;
                }

                clickerAnimationIndex = index;

                addCookie();

                if ((index + 1) <= shop.getClicker()) {
                    shop.setCookies(shop.getCookies() + 1);
                }

                index++;
            }
        }, 1, 1, TimeUnit.SECONDS);
        scheduleService(service, shop.getGrandmas(), 1);
        scheduleService(service, shop.getBakeries(), 4);
        scheduleService(service, shop.getFactories(), 10);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.21f/* + b*/, 0.53f/* + b*/, 0.70f/* + b*/, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);

        // Render mini cookies
        game.getBatch().begin();
        renderCookies();
        game.getBatch().end();

        renderClicker();

        // Render shop and info bar
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.LIGHT_GRAY);
        shapeRenderer.roundedRect(camera.position.x - (camera.viewportWidth / 4f),
                camera.position.y + (camera.viewportHeight / 2f) - 45,
                camera.viewportWidth / 2f, 40, 10);
        shapeRenderer.end();

        // Render everything else
        game.getBatch().begin();

        shopRepresentation.set(camera.position.x + (camera.viewportWidth / 4f) - 45,
                camera.position.y + (camera.viewportHeight / 2f) - 42, 35, 35);
        game.getBatch().draw(shopTexture, shopRepresentation.x, shopRepresentation.y,
                shopRepresentation.width, shopRepresentation.height);

        cookieRepresentation.set((camera.viewportWidth / 2f) - (COOKIE_WIDTH / 2f) - (COOKIE_WIDTH < 200 ? 5 : 0),
                (camera.viewportHeight / 2f) - (COOKIE_HEIGHT / 2f) - (COOKIE_HEIGHT < 200 ? 5 : 0), COOKIE_WIDTH, COOKIE_HEIGHT);
        game.getBatch().draw(cookieTexture, cookieRepresentation.x, cookieRepresentation.y,
                COOKIE_WIDTH, COOKIE_HEIGHT);

        game.getFont().draw(game.getBatch(), "Cookies per second: " + cookiesPerSecond,
                camera.position.x - (camera.viewportWidth / 2f) + 10, camera.position.y -
                        (camera.viewportHeight / 2f) + 100);
        game.getFont().draw(game.getBatch(), "Rendered cookies: " + amountMiniCookies,
                camera.position.x - (camera.viewportWidth / 2f) + 10, camera.position.y -
                        (camera.viewportHeight / 2f) + 70);
        game.getFont().draw(game.getBatch(), "FPS: " + Gdx.graphics.getFramesPerSecond(),
                camera.position.x - (camera.viewportWidth / 2f) + 10, camera.position.y -
                        (camera.viewportHeight / 2f) + 40);

        FontUtil.KOMIKA_20.draw(game.getBatch(), "Cookies: " + format.format(shop.getCookies()),
                camera.position.x - (camera.viewportWidth / 4f) + 10, camera.position.y +
                        (camera.viewportHeight / 2f) - 15);

        game.getBatch().end();

        shop.render(game, camera);

        // Remove disappeared cookies
        cookies.forEach(miniCookie -> {
            if (miniCookie.getY() <= -MINICOOKIE_HEIGHT) {
                cookies.remove(miniCookie);
                amountMiniCookies--;
            }
        });

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)
                && cookieRepresentation.contains(getUnprojectedScreenCoords(100))
                && shop.isNotVisible()) {
            COOKIE_HEIGHT -= 10;
            COOKIE_WIDTH -= 10;

            shop.setCookies(shop.getCookies() + 1);
            addCookie();
        } else if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)
                && COOKIE_WIDTH < 200 && COOKIE_HEIGHT < 200
                && shop.isNotVisible()) {
            COOKIE_WIDTH = 200;
            COOKIE_HEIGHT = 200;
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)
                && shopRepresentation.contains(getUnprojectedScreenCoords(0))
                && shop.isNotVisible()) {
            shop.setVisible(shop.isNotVisible());
        }
    }

    private void loadDataForShop() {
        Object[] objects = Data.loadProgress();
        shop.setCookies(objects[0] instanceof Long ? (Long) objects[0] : (Integer) objects[0]);
        shop.setClicker(objects[1] instanceof Long ? (Long) objects[1] : (Integer) objects[1]);
        shop.setGrandmas(objects[2] instanceof Long ? (Long) objects[2] : (Integer) objects[2]);
        shop.setBakeries(objects[3] instanceof Long ? (Long) objects[3] : (Integer) objects[3]);
        shop.setFactories(objects[4] instanceof Long ? (Long) objects[4] : (Integer) objects[4]);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> Data.saveProgress(
                shop.getCookies(),
                shop.getClicker(),
                shop.getGrandmas(),
                shop.getBakeries(),
                shop.getFactories()
        )));
    }

    private void scheduleService(ScheduledExecutorService service, long amount, int increase) {
        service.scheduleAtFixedRate(() -> {
            for (int i = 0; i < amount; i++) {
                try {
                    Thread.sleep(25);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                shop.setCookies(shop.getCookies() + increase);
                addCookie();
            }
        }, 500, 500, TimeUnit.MILLISECONDS);
    }

    private Vector2 getUnprojectedScreenCoords(float minus) {
        Vector3 screenCoords = new Vector3();
        screenCoords.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(screenCoords);

        return new Vector2(screenCoords.x - minus, screenCoords.y - minus);
    }

    private float getAngle(Vector2 source, Vector2 target) {
        float angle = (float) Math.toDegrees(Math.atan2(target.y - source.y, target.x - source.x));

        if (angle < 0) {
            angle += 360;
        }

        return angle;
    }

    private void renderClicker() {
        if (shop.getClicker() == 0) {
            return;
        }

        generalRotation += 0.1f;
        int row = 0;

        for (int i = 0; i < shop.getClicker(); i++) {
            if (i == 18 || i == 36) {
                row++;
            }

            float rotation = i * 20 + generalRotation;
            if (rotation > 360) {
                rotation = rotation % 360;
            }

            double angle = Math.toRadians(rotation);

            game.getBatch().begin();
            Vector2 center = new Vector2(
                    cookieRepresentation.x + COOKIE_MAX_WIDTH / 2f,
                    cookieRepresentation.y + COOKIE_MAX_HEIGHT / 2f
            );

            float x1 = cookieRepresentation.x + COOKIE_MAX_WIDTH / 2f - center.x;
            float y1 = center.y - COOKIE_MAX_HEIGHT / 2f - ((30 + (50 * row)) - (clickerAnimationIndex == i ? 10 : 0)) - center.y;

            float x2 = (float) (x1 * Math.cos(angle) - y1 * Math.sin(angle));
            float y2 = (float) (x1 * Math.sin(angle) + y1 * Math.cos(angle));

            Vector2 vec = new Vector2();
            vec.x = x2 + center.x;
            vec.y = y2 + center.y;

            game.getBatch().draw(
                    clickerTexture,
                    vec.x - (clickerTexture.getWidth() / 50f) / 2f,
                    vec.y - (clickerTexture.getHeight() / 50f) / 2f,
                    (clickerTexture.getWidth() / 50f) / 2f,
                    (clickerTexture.getHeight() / 50f) / 2f,
                    clickerTexture.getWidth() / 50f,
                    clickerTexture.getHeight() / 50f,
                    1,
                    1,
                    getAngle(
                            center,
                            new Vector2(
                                    vec.x - (clickerTexture.getWidth() / 50f) / 2f,
                                    vec.y - (clickerTexture.getHeight() / 50f) / 2f
                            )
                    ) + 90,
                    0,
                    0,
                    clickerTexture.getWidth(),
                    clickerTexture.getHeight(),
                    false,
                    false
            );
            game.getBatch().end();
        }
    }

    private void renderCookies() {
        cookies.forEach(miniCookie -> {
            game.getBatch().draw(cookieTexture, miniCookie.getX(), miniCookie.getY(), MINICOOKIE_WIDTH, MINICOOKIE_HEIGHT);
            miniCookie.setY(miniCookie.getY() - MINICOOKIE_SPEED);
            miniCookie.setRotation((miniCookie.getRotation() + MINICOOKIE_ROTATION_SPEED) % 360.0f);
        });
    }

    private void addCookie() {
        if (MINICOOKIE_THRESHOLD == -1 || amountMiniCookies <= MINICOOKIE_THRESHOLD) {
            cookies.add(new MiniCookie(MathUtils.random(5, camera.viewportWidth - 30), camera.viewportHeight + MINICOOKIE_HEIGHT, MathUtils.random(0.0f, 360.0f)));
            amountMiniCookies++;
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        shop.dispose();
        cookieTexture.dispose();
        clickerTexture.dispose();
        shopTexture.dispose();
    }
}