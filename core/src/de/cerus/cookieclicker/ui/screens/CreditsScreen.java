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
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import de.cerus.cookieclicker.CookieClickerGame;
import de.cerus.cookieclicker.util.ContributorUtil;
import de.cerus.cookieclicker.util.FontUtil;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class CreditsScreen implements Screen {
    private CookieClickerGame game;
    private OrthographicCamera camera;

    public CreditsScreen(CookieClickerGame game) {
        this.game = game;
        this.camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void show() {
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render(float delta) {
        camera.update();
        game.getBatch().begin();

        FontUtil.KOMIKA.draw(game.getBatch(), "Press ESC to go back", camera.position.x - (camera.viewportWidth / 2f) + 10,
                camera.position.y - (camera.viewportHeight / 2f) + 30);
        if (ContributorUtil.isLoading()) {
            FontUtil.KOMIKA.draw(game.getBatch(), "Loading contributors...", camera.position.x - (camera.viewportWidth / 2f) + 10, camera.position.y);
        } else {
            FontUtil.KOMIKA.draw(game.getBatch(), "Want to contribute?", camera.position.x + (camera.viewportWidth / 2f) - 220,
                    camera.position.y + (camera.viewportHeight / 2f - 10));
            FontUtil.KOMIKA.draw(game.getBatch(), "This project is open source:",
                    camera.position.x + (camera.viewportWidth / 2f) - 280,
                    camera.position.y + (camera.viewportHeight / 2f - 45));
            FontUtil.KOMIKA.draw(game.getBatch(), "https://git.io/JeEIx",
                    camera.position.x + (camera.viewportWidth / 2f) - 220,
                    camera.position.y + (camera.viewportHeight / 2f - 80));

            for (int i = 0; i < ContributorUtil.getContributors().size(); i++) {
                ContributorUtil.ContributorData data = ContributorUtil.getContributors().get(i);
                FontUtil.KOMIKA.draw(
                        game.getBatch(),
                        data.getName() + ": " + data.getCommits() + " commit(s)",
                        camera.position.x - (camera.viewportWidth / 2f) + 10,
                        camera.position.y + (camera.viewportHeight / 2f - 10 - (i * 35))
                );
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(game.getScreenContext().getMenuScreen());
        } else if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Vector3 vec = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(vec);

            if (vec.y > camera.position.y + (camera.viewportHeight / 2f - 80 - 30)
                    && vec.y < camera.position.y + (camera.viewportHeight / 2f - 80)
                    && vec.x > camera.position.x + (camera.viewportWidth / 2f) - 220
                    && vec.x < camera.position.x + (camera.viewportWidth / 2f)) {

                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.browse(new java.net.URL("https://git.io/JeEIx").toURI());
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }

        game.getBatch().end();
    }

    @Override
    public void resize(int width, int height) {

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

    }
}
