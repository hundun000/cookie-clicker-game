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
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;
import de.cerus.cookieclicker.CookieClickerGame;
import de.cerus.cookieclicker.util.DisposeUtil;

public class MiniCookie implements Disposable {

    private static float WIDTH = 25;
    private static float HEIGHT = 25;

    private OrthographicCamera camera;
    private float x;
    private float y;
    private float rotation;
    private float speed;
    private Texture texture;

    public MiniCookie(OrthographicCamera camera) {
        this.camera = camera;
        y = camera.viewportHeight + WIDTH;
        x = MathUtils.random(5, camera.viewportWidth - 5 - WIDTH);
        rotation = MathUtils.random(0.0f, 360.0f);
        speed = MathUtils.random(0.6f, 2f);
        String texture_file = ((MathUtils.random(0,1) > 0.5) ? "cookie.png" : "chocolate_cookie.png");
        texture = new Texture(Gdx.files.internal(texture_file));
    }

    public void render(CookieClickerGame game) {
        game.getBatch().draw(texture, x, y, WIDTH, HEIGHT);

        y -= 0.8 * speed;

        rotation += 0.25f;
        if (rotation >= 360.0f) rotation = 0.0f;
    }

    public boolean isVisible() {
        return y > 0 - HEIGHT;
    }

    @Override
    public void dispose() {
        DisposeUtil.dispose(this);
    }
}
