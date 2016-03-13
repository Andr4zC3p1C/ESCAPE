package com.kingcoder.escape.desktop;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.kingcoder.escape.Main;

public class DesktopLauncher {
	
	private static int width = 1280, height = 720;
	private static boolean FULL_SCREEN = false;
	
	public static void main (String[] arg) {
		System.setProperty("user.name","CorrectUserName");


		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.foregroundFPS = 0;
		config.backgroundFPS = -1;
		loadConfig();
		
		if(FULL_SCREEN){
			GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			width = gd.getDisplayMode().getWidth();
			height = gd.getDisplayMode().getHeight();
			config.vSyncEnabled = true;
			config.fullscreen = true;
		}else{
			config.vSyncEnabled = false;
		}
		
		config.width = width;
		config.height = height;
		config.useGL30 = false;
		config.resizable = false;
		config.title = Main.TITLE + "  " + Main.VERSION;
		
		Main.WIDTH = width;
		Main.HEIGHT = height;
		
		new LwjglApplication(new Main(), config);
	}
	
	private static void loadConfig(){
		try {
			String userDirPath = System.getProperty("user.home").replace('\\', '/') + "/Documents/Escape_userDir/";
			File userDir = new File(userDirPath);
			
			if(!userDir.exists()){
				userDir.mkdirs();
			}
			
			userDir = new File(System.getProperty("user.home").replace('\\', '/') + "/Documents/Escape_userDir/settings.config");
			if(!userDir.exists()){
				createDefaultSettingsFile();
				FULL_SCREEN = true;
				return;
			}
			
			// reading settings.config
			FileReader fr = new FileReader(userDir);
			BufferedReader br = new BufferedReader(fr);
			
			// the settings
			int full_screen_bool = Integer.parseInt(br.readLine().split(" = ")[1]);
			width = Integer.parseInt(br.readLine().split(" = ")[1]);
			height = Integer.parseInt(br.readLine().split(" = ")[1]);
			
			br.close();
			
			switch(full_screen_bool){
			case 1:
				FULL_SCREEN = true; 
				break;
				
			case 0:
				FULL_SCREEN = false;
				break;
				
			default:
					System.out.println("Please write a valid value(1 or 0) for FULL_SCREEN propertie in <user_home>/Documents/Escape_userDir/settings.config");
					FULL_SCREEN = true;
					createDefaultSettingsFile();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void createDefaultSettingsFile() throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter pw = new PrintWriter(System.getProperty("user.home").replace('\\', '/') + "/Documents/Escape_userDir/settings.config", "UTF-8");
		pw.println("FULL_SCREEN = 1");
		pw.println("RESOLUTION_WIDTH = 1280");
		pw.println("RESOLUTION_HEIGHT = 720");
		pw.close();
	}
}
