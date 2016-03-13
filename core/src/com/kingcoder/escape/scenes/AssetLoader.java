package com.kingcoder.escape.scenes;


import java.io.File;
import java.net.URL;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kingcoder.escape.Main;

public class AssetLoader extends Scene implements Runnable{
	private float progress = 0.0f;
	
	private String[] textures;
	private Pixmap[] pixmaps;
	
    public void init(){
    	sceneID = SceneID.TextureLoader; 
        initialized = true;
        
        // rendering the loading
        Pixmap pixels = new Pixmap(100, 100, Format.RGBA8888);
        
        new Thread(this).start();
    }

    public void update(){
    	if(progress == 1.0f){
    		// loading the textures into GPU RAM and adding them all into HashMaps
    		for(int i=0; i < textures.length; i++){
    			Main.textureManager.createTexture(pixmaps[i], textures[i]);
    			pixmaps[i].dispose();
    		}
    		
    		pixmaps = null;
    		
    		// starting the game
            Main.sceneManager.setCurrentScene(new Game()); // TODO: change to MainMenu() when done
    	}
    }

    // separate thread loading
    public void run(){
        // loading all textures into RAM
    	//URL url = getClass().getResource("/textures/");
    	// FIle dir = new File(url.getPath());
    	
    	// loading the texture names
    	textures = new String[]{"player", "projectiles", "tileset"};
    	int numTextures = 3;
        
        // loading the pixmaps
    	pixmaps = new Pixmap[numTextures];
        for(int i = 0; i < numTextures; i++){
            pixmaps[i] = (new Pixmap(Gdx.files.internal("textures/" + textures[i] + "/" + textures[i] + ".png")));
            progress += 1.0f / numTextures;
        }
        
        progress = 1.0f;
    }
    
    public void dispose(){}

	public void renderDynamic(SpriteBatch dynamicBatch) {
		
	}

	public void renderStatic(SpriteBatch staticBatch) {
		Main.renderer.font_title.draw(staticBatch, "LOADING...", Main.WIDTH - 400, 100);
	}
}
