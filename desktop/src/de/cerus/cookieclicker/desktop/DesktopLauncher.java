package de.cerus.cookieclicker.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.cerus.cookieclicker.CookieClickerGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.backgroundFPS = 30;
		config.title = "Cookie Clicker";
		config.width = 854;
		config.height = 480;

		new LwjglApplication(new CookieClickerGame(), config);
	}
}
