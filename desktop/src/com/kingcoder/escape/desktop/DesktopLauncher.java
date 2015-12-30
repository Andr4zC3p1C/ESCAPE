package com.kingcoder.escape.desktop;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.kingcoder.escape.Main;

public class DesktopLauncher {
	
	private static int width = 1280, height = 720;
	
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.vSyncEnabled = false;
		config.foregroundFPS = 0;
		config.backgroundFPS = -1;
		
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		//width = gd.getDisplayMode().getWidth();
		//height = gd.getDisplayMode().getHeight();
		
		config.width = width;
		config.height = height;
		config.fullscreen = false;
		config.useGL30 = true;
		config.title = Main.TITLE + "  " + Main.VERSION;
		
		Main.WIDTH = width;
		Main.HEIGHT = height;
		
		new LwjglApplication(new Main(), config);
	}
	
	private static void loadConfig(){
		// TODO: load '.config' file from user home and see if full_screen or not
	}
}
