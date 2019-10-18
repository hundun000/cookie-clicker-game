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
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Array;
import de.cerus.cookieclicker.CookieClickerGame;
import de.cerus.cookieclicker.components.Menu;
import de.cerus.cookieclicker.util.DisposeUtil;

public class ExitScreen implements Screen {

    private CookieClickerGame game;
    private OrthographicCamera camera;
    private Menu<String> menu;

    private GlyphLayout exitText;

    public ExitScreen(CookieClickerGame game) {
        this.game = game;
        this.camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.menu = new Menu<>(game.getFont(), new Array<String>() {
            {
                add("Yes");
                add("No");
            }
        });
        this.exitText = new GlyphLayout(game.getFont(), "Are you sure you want to exit?");
    }

    @Override
    public void show() {
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        menu.setOnAction(s -> {
            if(s.equals("Yes")) {
                Gdx.app.exit();
                return;
            }
            game.setScreen(new MenuScreen(game));
        });
    }

    @Override
    public void render(float delta) {
        game.getBatch().setProjectionMatrix(camera.combined);
        game.getBatch().begin();

        game.getFont().draw(game.getBatch(), exitText, camera.viewportWidth / 2f - (exitText.width / 2f),
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
        DisposeUtil.dispose(this);
    }
}
