package com.kingcoder.dungeon_crawler.scenes;


import java.awt.Graphics;

import com.kingcoder.dungeon_crawler.Main;
import com.kingcoder.dungeon_crawler.handlers.ErrorLogger;

public class TextureLoader extends Scene implements Runnable{
    private Thread loadThread;
    private int progress = -1;
    private String[] textures;

    public void init(){
    	sceneID = SceneID.TextureLoader; 
        loadThread = new Thread(this);
        initialized = true;
    }

    public void update(){
        if(progress == 1){
            Main.sceneManager.setCurrentScene(new Game()); // TODO: change to MainMenu() when done
        }else if(progress == -1){
            progress = 0;
            loadThread.start();
        }else if(progress == 10){
            progress = 1;
            try {
				loadThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
    }

    public void render(){
    	// no loading screen
    }
    
    public void renderHighRes(Graphics g){
        // no loading screen
    }

    public void dispose(){
        try {
            loadThread.join();
        }catch(Exception e){
        	ErrorLogger.log(e);
        }
    }


    public void run(){
    	// TODO: make auto-detect folders and create an array of textures
        // loading all textures into RAM
        textures = new String[3];
        textures[0] = "player";
        textures[1] = "tileset";
        textures[2] = "projectiles";
        
        for(int i = 0; i < textures.length; i++){
            Main.textureManager.loadSpriteSheet(textures[i]);
        }
        
        progress = 10;
    }
}
