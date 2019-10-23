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

package de.cerus.cookieclicker.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import de.cerus.cookieclicker.CookieClickerGame;
import de.cerus.cookieclicker.fixes.CustomShapeRenderer;
import de.cerus.cookieclicker.util.FontUtil;

public class Shop implements Disposable {

    public static final int MAX_CLICKER = 54;
    public static final int MAX_GRANDMAS = 20;
    public static final int MAX_BAKERIES = 20;
    public static final int MAX_FACTORIES = 20;

    private CustomShapeRenderer shapeRenderer = new CustomShapeRenderer();
    private boolean visible = false;

    private Texture closeTexture = new Texture(Gdx.files.internal("close.png"));
    private Texture buyTexture = new Texture(Gdx.files.internal("buy.png"));

    public Rectangle closeRepresentation = new Rectangle();
    public Rectangle buyClickerRepresentation = new Rectangle();
    public Rectangle buyGrandmaRepresentation = new Rectangle();
    public Rectangle buyBakeryRepresentation = new Rectangle();
    public Rectangle buyFactoryRepresentation = new Rectangle();

    private float buyClickerButtonWidth = buyTexture.getWidth();
    private float buyClickerButtonHeight = buyTexture.getHeight();
    private float buyGrandmaButtonWidth = buyTexture.getWidth();
    private float buyGrandmaButtonHeight = buyTexture.getHeight();
    private float buyBakeryButtonWidth = buyTexture.getWidth();
    private float buyBakeryButtonHeight = buyTexture.getHeight();
    private float buyFactoryButtonWidth = buyTexture.getWidth();
    private float buyFactoryButtonHeight = buyTexture.getHeight();

    private float animationAlpha = 0.0f;
    private long cookies = 0;
    private long clicker = 0;
    private long grandmas = 0;
    private long bakeries = 0;
    private long factories = 0;

    public void render(CookieClickerGame game, OrthographicCamera camera) {
        if (!visible) {
            return;
        }

        if (animationAlpha > 0) {
            animationAlpha -= 0.10;
        }

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(140 / 255f, 140 / 255f, 140 / 255f, 1.0f - animationAlpha);
        shapeRenderer.roundedRect(
                camera.position.x - (camera.viewportWidth / 2.2f),
                camera.position.y - (camera.viewportHeight / 2.2f),
                camera.viewportWidth / 2.2f * 2,
                camera.viewportHeight / 2.2f * 2, 10
        );
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        closeRepresentation.set(
                camera.position.x - (camera.viewportWidth / 2.2f) + camera.viewportWidth / 2.2f * 2 - 30,
                camera.position.y + (camera.viewportHeight / 2.2f) - 30,
                25,
                25
        );


        game.getBatch().begin();

        game.getBatch().setColor(game.getBatch().getColor().add(0, 0, 0, -animationAlpha));
        game.getBatch().draw(
                closeTexture,
                closeRepresentation.x,
                closeRepresentation.y,
                closeRepresentation.width,
                closeRepresentation.height
        );
        game.getBatch().setColor(1, 1, 1, 1);

        if (clicker >= MAX_CLICKER || cookies < 5) {
            FontUtil.KOMIKA.setColor(Color.RED);
            game.getBatch().setColor(game.getBatch().getColor().add(0, 0, 0, -0.5f));
        }
        FontUtil.KOMIKA.draw(
                game.getBatch(),
                "Clicker: " + clicker + " / " + MAX_CLICKER + " [Cost: 5]",
                camera.position.x - (camera.viewportWidth / 2.2f) + 20,
                camera.position.y + (camera.viewportHeight / 2.2f) - 30
        );
        FontUtil.KOMIKA.setColor(Color.WHITE);
        buyClickerRepresentation.set(
                camera.position.x - (camera.viewportWidth / 2.2f) + camera.viewportWidth / 2.2f * 2 - 110 - buyClickerButtonWidth / 2f,
                camera.position.y + (camera.viewportHeight / 2.2f) - 40 - buyClickerButtonHeight / 2f,
                buyClickerButtonWidth,
                buyClickerButtonHeight
        );
        game.getBatch().draw(
                buyTexture,
                buyClickerRepresentation.x,
                buyClickerRepresentation.y,
                buyClickerRepresentation.width,
                buyClickerRepresentation.height
        );
        game.getBatch().setColor(Color.WHITE);

        if (grandmas >= MAX_GRANDMAS || cookies < 100) {
            FontUtil.KOMIKA.setColor(Color.RED);
            game.getBatch().setColor(game.getBatch().getColor().add(0, 0, 0, -0.5f));
        }
        FontUtil.KOMIKA.draw(
                game.getBatch(),
                "Grandma: " + grandmas + " / " + MAX_GRANDMAS + " [Cost: 100]",
                camera.position.x - (camera.viewportWidth / 2.2f) + 20,
                camera.position.y + (camera.viewportHeight / 2.2f) - 60
        );
        FontUtil.KOMIKA.setColor(Color.WHITE);
        buyGrandmaRepresentation.set(
                camera.position.x - (camera.viewportWidth / 2.2f) + camera.viewportWidth / 2.2f * 2 - 110 - buyGrandmaButtonWidth / 2f,
                camera.position.y + (camera.viewportHeight / 2.2f) - 70 - buyGrandmaButtonHeight / 2f,
                buyGrandmaButtonWidth,
                buyGrandmaButtonHeight
        );
        game.getBatch().draw(
                buyTexture,
                buyGrandmaRepresentation.x,
                buyGrandmaRepresentation.y,
                buyGrandmaRepresentation.width,
                buyGrandmaRepresentation.height
        );
        game.getBatch().setColor(Color.WHITE);

        if (bakeries >= MAX_BAKERIES || cookies < 250) {
            FontUtil.KOMIKA.setColor(Color.RED);
            game.getBatch().setColor(game.getBatch().getColor().add(0, 0, 0, -0.5f));
        }
        FontUtil.KOMIKA.draw(
                game.getBatch(),
                "Bakery: " + bakeries + " / " + MAX_BAKERIES + " [Cost: 250]",
                camera.position.x - (camera.viewportWidth / 2.2f) + 20,
                camera.position.y + (camera.viewportHeight / 2.2f) - 90
        );
        FontUtil.KOMIKA.setColor(Color.WHITE);
        buyBakeryRepresentation.set(
                camera.position.x - (camera.viewportWidth / 2.2f) + camera.viewportWidth / 2.2f * 2 - 110 - buyBakeryButtonWidth / 2f,
                camera.position.y + (camera.viewportHeight / 2.2f) - 100 - buyBakeryButtonHeight / 2f,
                buyBakeryButtonWidth,
                buyBakeryButtonHeight
        );
        game.getBatch().draw(
                buyTexture,
                buyBakeryRepresentation.x,
                buyBakeryRepresentation.y,
                buyBakeryRepresentation.width,
                buyBakeryRepresentation.height
        );
        game.getBatch().setColor(Color.WHITE);

        if (factories >= MAX_FACTORIES || cookies < 1000) {
            FontUtil.KOMIKA.setColor(Color.RED);
            game.getBatch().setColor(game.getBatch().getColor().add(0, 0, 0, -0.5f));
        }
        FontUtil.KOMIKA.draw(
                game.getBatch(),
                "Factory: " + factories + " / " + MAX_FACTORIES + " [Cost: 1000]",
                camera.position.x - (camera.viewportWidth / 2.2f) + 20,
                camera.position.y + (camera.viewportHeight / 2.2f) - 120
        );
        FontUtil.KOMIKA.setColor(Color.WHITE);
        buyFactoryRepresentation.set(
                camera.position.x - (camera.viewportWidth / 2.2f) + camera.viewportWidth / 2.2f * 2 - 110 - buyFactoryButtonWidth / 2f,
                camera.position.y + (camera.viewportHeight / 2.2f) - 130 - buyFactoryButtonHeight / 2f,
                buyFactoryButtonWidth,
                buyFactoryButtonHeight
        );
        game.getBatch().draw(
                buyTexture,
                buyFactoryRepresentation.x,
                buyFactoryRepresentation.y,
                buyFactoryRepresentation.width,
                buyFactoryRepresentation.height
        );
        game.getBatch().setColor(Color.WHITE);

        game.getBatch().end();


        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)
                && closeRepresentation.contains(getUnprojectedScreenCoords(camera, 0))
                || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            setVisible(false);
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)
                && buyClickerRepresentation.contains(getUnprojectedScreenCoords(camera, 0))
                && clicker < MAX_CLICKER
                && cookies >= 5) {
            cookies -= 5;
            clicker++;

            buyClickerButtonHeight -= 5;
            buyClickerButtonWidth -= 5;
        } else if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)
                && buyClickerButtonWidth < buyTexture.getWidth()
                && buyClickerButtonHeight < buyTexture.getHeight()) {
            buyClickerButtonWidth = buyTexture.getWidth();
            buyClickerButtonHeight = buyTexture.getHeight();
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)
                && buyGrandmaRepresentation.contains(getUnprojectedScreenCoords(camera, 0))
                && grandmas < MAX_GRANDMAS
                && cookies >= 100) {
            cookies -= 100;
            grandmas++;

            buyGrandmaButtonHeight -= 5;
            buyGrandmaButtonWidth -= 5;
        } else if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)
                && buyGrandmaButtonWidth < buyTexture.getWidth()
                && buyGrandmaButtonHeight < buyTexture.getHeight()) {
            buyGrandmaButtonWidth = buyTexture.getWidth();
            buyGrandmaButtonHeight = buyTexture.getHeight();
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)
                && buyBakeryRepresentation.contains(getUnprojectedScreenCoords(camera, 0))
                && bakeries < MAX_BAKERIES
                && cookies >= 250) {
            cookies -= 250;
            bakeries++;

            buyBakeryButtonHeight -= 5;
            buyBakeryButtonWidth -= 5;
        } else if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)
                && buyBakeryButtonWidth < buyTexture.getWidth()
                && buyBakeryButtonHeight < buyTexture.getHeight()) {
            buyBakeryButtonWidth = buyTexture.getWidth();
            buyBakeryButtonHeight = buyTexture.getHeight();
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)
                && buyFactoryRepresentation.contains(getUnprojectedScreenCoords(camera, 0))
                && factories < MAX_FACTORIES
                && cookies >= 1000) {
            cookies -= 1000;
            factories++;

            buyFactoryButtonHeight -= 5;
            buyFactoryButtonWidth -= 5;
        } else if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)
                && buyFactoryButtonWidth < buyTexture.getWidth()
                && buyFactoryButtonHeight < buyTexture.getHeight()) {
            buyFactoryButtonWidth = buyTexture.getWidth();
            buyFactoryButtonHeight = buyTexture.getHeight();
        }
    }

    private Vector2 getUnprojectedScreenCoords(Camera camera, float minus) {
        Vector3 screenCoords = new Vector3();
        screenCoords.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(screenCoords);

        return new Vector2(screenCoords.x - minus, screenCoords.y - minus);
    }

    public boolean isNotVisible() {
        return !visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        if (visible) {
            animationAlpha = 1.0f;
        }
    }

    public long getClicker() {
        return clicker;
    }

    public void setClicker(long clicker) {
        this.clicker = clicker;
    }

    public long getCookies() {
        return cookies;
    }

    public void setCookies(long cookies) {
        this.cookies = cookies;
    }

    public long getGrandmas() {
        return grandmas;
    }

    public void setGrandmas(long grandmas) {
        this.grandmas = grandmas;
    }

    public long getBakeries() {
        return bakeries;
    }

    public void setBakeries(long bakeries) {
        this.bakeries = bakeries;
    }

    public long getFactories() {
        return factories;
    }

    public void setFactories(long factories) {
        this.factories = factories;
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        closeTexture.dispose();
        buyTexture.dispose();
    }
}