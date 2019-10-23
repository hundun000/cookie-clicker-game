package de.cerus.cookieclicker;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.cerus.cookieclicker.screens.GameScreen;
import de.cerus.cookieclicker.screens.MenuScreen;
import de.cerus.cookieclicker.util.FontUtil;

public class CookieClickerGame extends Game {

    private SpriteBatch batch;

    private BitmapFont font;

    @Override
    public void create() {
        FontUtil.init();

        batch = new SpriteBatch();
        font = FontUtil.KOMIKA;

        setScreen(new MenuScreen(this));
    }

    @Override
    public void render() {
        if (screen == null || !(screen instanceof GameScreen)) {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        }

        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        font.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public BitmapFont getFont() {
        return font;
    }
}