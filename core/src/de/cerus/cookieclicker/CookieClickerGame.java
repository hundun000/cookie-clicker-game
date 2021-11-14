package de.cerus.cookieclicker;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.cerus.cookieclicker.ui.screens.CreditsScreen;
import de.cerus.cookieclicker.ui.screens.ExitScreen;
import de.cerus.cookieclicker.ui.screens.GameScreen;
import de.cerus.cookieclicker.ui.screens.MenuScreen;
import de.cerus.cookieclicker.ui.screens.ScreenContext;
import de.cerus.cookieclicker.ui.screens.SettingsScreen;
import de.cerus.cookieclicker.util.ContributorUtil;
import de.cerus.cookieclicker.util.FontUtil;
import lombok.Getter;

public class CookieClickerGame extends Game {

    private SpriteBatch batch;

    private BitmapFont font;

    @Getter
    ScreenContext screenContext;
    
    
    
    @Override
    public void create() {
        FontUtil.init();
        ContributorUtil.load();

        batch = new SpriteBatch();
        font = FontUtil.KOMIKA;

        screenContext = new ScreenContext();
        screenContext.setCreditsScreen(new CreditsScreen(this));
        screenContext.setExitScreen(new ExitScreen(this));
        screenContext.setGameScreen(new GameScreen(this));
        screenContext.setMenuScreen(new MenuScreen(this));
        screenContext.setSettingsScreen(new SettingsScreen(this));
        
        setScreen(screenContext.getMenuScreen());
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