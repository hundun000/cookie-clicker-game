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
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Array;
import de.cerus.cookieclicker.CookieClickerGame;
import de.cerus.cookieclicker.ui.other.Menu;

public class MenuScreen implements Screen {

    private CookieClickerGame game;
    private OrthographicCamera camera;
    private Menu<String> menu;

    private GlyphLayout menuText;

    public MenuScreen(CookieClickerGame game) {
        this.game = game;
        this.camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.menu = new Menu<>(game.getFont(), new Array<String>() {
            {
                add("Play");
                add("Settings");
                add("Credits");
                add("Exit");
            }
        });
        this.menuText = new GlyphLayout(game.getFont(), "Menu");
    }

    @Override
    public void show() {
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        menu.setOnAction(s -> {
            switch (s) {
                case "Play":
                    game.setScreen(game.getScreenContext().getGameScreen());
                    break;
                case "Settings":
                    game.setScreen(game.getScreenContext().getSettingsScreen());
                    break;
                case "Credits":
                    game.setScreen(game.getScreenContext().getCreditsScreen());
                    break;
                case "Exit":
                    game.setScreen(game.getScreenContext().getExitScreen());
                    break;
            }
        });
    }

    @Override
    public void render(float delta) {
        game.getBatch().setProjectionMatrix(camera.combined);
        game.getBatch().begin();

        game.getFont().draw(game.getBatch(), menuText, camera.viewportWidth / 2f - (menuText.width / 2f),
                camera.viewportHeight - 50);

        menu.render(game.getBatch(), true, true, camera.viewportWidth / 2f,
                camera.viewportHeight - 100, 0, -30);

        game.getBatch().end();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {}
}