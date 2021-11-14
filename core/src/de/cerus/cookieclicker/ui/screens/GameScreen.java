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

package de.cerus.cookieclicker.ui.screens;

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
import de.cerus.cookieclicker.model.GameScreenModel;
import de.cerus.cookieclicker.model.ModelContext;
import de.cerus.cookieclicker.ui.other.CustomShapeRenderer;
import de.cerus.cookieclicker.ui.other.component.CookieBoardUI;
import de.cerus.cookieclicker.ui.other.component.CookieUI;
import de.cerus.cookieclicker.ui.other.component.ShopUI;
import de.cerus.cookieclicker.util.FontUtil;
import de.cerus.cookieclicker.util.SaveUtil;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Supplier;

public class GameScreen implements Screen {

    private static final float LOGIC_FRAME_LENGTH      = 1 / 30f; 
    //final int   MAX_FRAMESKIPS = 5;       // Make 5 updates mostly without a render
    
    GameScreenModel model;
    
    ShopUI shopUI;
    CookieBoardUI cookieBoardUI;
    
    private float accumulator;
    
    


    private CustomShapeRenderer shapeRenderer;

    private CookieClickerGame game;
    private OrthographicCamera camera;
    
    private DecimalFormat format;

    
    private Texture shopTexture;
    

    
    private Rectangle shopRepresentation;








    

    private ModelContext modelContext;
    
    public GameScreen(CookieClickerGame game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        
        this.format = new DecimalFormat("#,###");

        
        this.shopTexture = new Texture(Gdx.files.internal("shop.png"));
        

        
        this.shopRepresentation = new Rectangle();

        this.shapeRenderer = new CustomShapeRenderer();
        
        this.modelContext = new ModelContext();
        
        this.shopUI = new ShopUI(modelContext);
        this.cookieBoardUI = new CookieBoardUI(this, game, modelContext);
        
        this.model = new GameScreenModel(this, modelContext);
    }

    @Override
    public void show() {
        //System.out.println("GameScreen show() called");
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        model.loadDataForShop();

        model.setClickerAnimationIndex(0);

    }
    

    @Override
    public void render(float delta) {
        
        cookieBoardUI.inputLogicFrame();
        shopUI.inputLogicFrame(camera);
        this.inputLogicFrame();
        
        accumulator += delta;
        if (accumulator >= LOGIC_FRAME_LENGTH) {
            accumulator -= LOGIC_FRAME_LENGTH;
            model.logicFrame();
            cookieBoardUI.cookiesLogicFrame();
        }
        
        
        Gdx.gl.glClearColor(0.21f/* + b*/, 0.53f/* + b*/, 0.70f/* + b*/, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);
    
        
        
        cookieBoardUI.boardRender(camera);
        this.renderInfoBar();
        shopUI.render(game, camera);
    }

    private void renderInfoBar() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.LIGHT_GRAY);
        shapeRenderer.roundedRect(camera.position.x - (camera.viewportWidth / 4f),
                camera.position.y + (camera.viewportHeight / 2f) - 45,
                camera.viewportWidth / 2f, 40, 10);
        shapeRenderer.end();


        game.getBatch().begin();

        shopRepresentation.set(camera.position.x + (camera.viewportWidth / 4f) - 45,
                camera.position.y + (camera.viewportHeight / 2f) - 42, 35, 35);
        game.getBatch().draw(shopTexture, shopRepresentation.x, shopRepresentation.y,
                shopRepresentation.width, shopRepresentation.height);

        cookieBoardUI.renderPart2(camera);
        


        game.getFont().draw(game.getBatch(), "Cookies per second: " + model.getCookiesPerSecond(),
                camera.position.x - (camera.viewportWidth / 2f) + 10, camera.position.y -
                        (camera.viewportHeight / 2f) + 100);
        game.getFont().draw(game.getBatch(), "Rendered cookies: " + cookieBoardUI.getSize(),
                camera.position.x - (camera.viewportWidth / 2f) + 10, camera.position.y -
                        (camera.viewportHeight / 2f) + 70);
        game.getFont().draw(game.getBatch(), "FPS: " + Gdx.graphics.getFramesPerSecond(),
                camera.position.x - (camera.viewportWidth / 2f) + 10, camera.position.y -
                        (camera.viewportHeight / 2f) + 40);

        FontUtil.KOMIKA_20.draw(game.getBatch(), "Cookies: " + format.format(model.shopGetCookies()),
                camera.position.x - (camera.viewportWidth / 4f) + 10, camera.position.y +
                        (camera.viewportHeight / 2f) - 15);

        game.getBatch().end();
    }

    private void inputLogicFrame() {


        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)
                && shopRepresentation.contains(getUnprojectedScreenCoords(0))
                && model.shopIsNotVisible()) {
            model.shopToggleVisible();
        }
    }

    





    public Vector2 getUnprojectedScreenCoords(float minus) {
        Vector3 screenCoords = new Vector3();
        screenCoords.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(screenCoords);

        return new Vector2(screenCoords.x - minus, screenCoords.y - minus);
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
        shopUI.dispose();
        cookieBoardUI.dispose();
        shopTexture.dispose();
    }

    public void addCookieUI() {
        cookieBoardUI.addCookieUI(camera);
    }
}