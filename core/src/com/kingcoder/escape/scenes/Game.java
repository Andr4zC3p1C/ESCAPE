package com.kingcoder.escape.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kingcoder.escape.Main;
import com.kingcoder.escape.entities.Entity_ID;
import com.kingcoder.escape.entities.Player;
import com.kingcoder.escape.entities.Skeleton;
import com.kingcoder.escape.game_managers.EntityManager;
import com.kingcoder.escape.game_managers.HUDManager;
import com.kingcoder.escape.game_managers.MapObjectManager;
import com.kingcoder.escape.game_managers.TileMapManager;
import com.kingcoder.escape.game_managers.UIManager;
import com.kingcoder.escape.math.Rect;
import com.kingcoder.escape.math.Vector2f;
import com.kingcoder.escape.util.ErrorLogger;

public class Game extends Scene implements Runnable{
    public static TileMapManager tileMapManager;
    public static MapObjectManager mapObjectManager;
    public static EntityManager entityManager;
    public static HUDManager hudManager;
    public static UIManager uiManager;

    // input for the game
    public static Vector2f mousePos;
    
    // level to be loaded
    private String level;
    
    // loading
    private Thread loadThread;
    private int loadProgress = -1;
    
    // game state 
    private static boolean paused = false;

    // chunks
    public static int CHUNK_SIZE = 0; // the size of one chunk in pixels
    private static Rect chunks; // weather they are supposed to be rendered and updated or not
    
    public void init(){
        sceneID = SceneID.Game;
        
        // test level
        level = "test_level";

        tileMapManager = new TileMapManager();
        mapObjectManager = new MapObjectManager();
        entityManager = new EntityManager();
        hudManager = new HUDManager();
        uiManager = new UIManager();

        mousePos = new Vector2f(0,0);
        
        loadThread = new Thread(this);
        initialized = true;
    }

    private void initChunks(){
    	CHUNK_SIZE = tileMapManager.getTileSize();
    	chunks = new Rect(0, 0, 0, 0);
    }
    
    public void update(){
    	// handle any input
    	mousePos.x = Gdx.input.getX();
    	mousePos.y = Main.HEIGHT - Gdx.input.getY();
    	
        if(loadProgress == 1){
        	// the gameplay update
        	if(!paused){
	        	updateChunks();
	        	tileMapManager.update();
	        	mapObjectManager.update();
	        	entityManager.update();
	        	hudManager.update();
        	}
        	
        	// the menu UI update
        	uiManager.update();
        }else if(loadProgress == -1){
        	loadProgress = 0;
            loadThread.start();
        }else if(loadProgress == 10){
        	loadProgress = 1;
        	
        	tileMapManager.initTexture();
        	
        	try {
				loadThread.join();
			} catch (InterruptedException e) {
				ErrorLogger.log(e);
			}
        }
    }

    
    private void updateChunks(){
    	Vector2f startPoint = new Vector2f(Main.renderer.cameraPos.x - Main.WIDTH/2, Main.renderer.cameraPos.y - Main.HEIGHT/2);
    	Vector2f endPoint = new Vector2f(Main.renderer.cameraPos.x + Main.WIDTH/2, Main.renderer.cameraPos.y + Main.HEIGHT/2);
    	
    	chunks.setX(((int)startPoint.x / CHUNK_SIZE) * CHUNK_SIZE);
    	chunks.setY(((int)startPoint.y / CHUNK_SIZE) * CHUNK_SIZE);
    	chunks.setWidth((int)Main.WIDTH / CHUNK_SIZE * CHUNK_SIZE);
    	chunks.setHeight((int)Main.HEIGHT / CHUNK_SIZE * CHUNK_SIZE);
    }
    
    public void renderDynamic(SpriteBatch dynamicBatch){
    	if(loadProgress == 1){
        	tileMapManager.render(dynamicBatch);
        	mapObjectManager.render(dynamicBatch);
        	entityManager.render(dynamicBatch);
        }
    }
    
    public void renderStatic(SpriteBatch staticBatch){
    	if(loadProgress == 1){
    		
    	}else{
    		Main.renderer.font_title.draw(staticBatch, "LOADING...", Main.WIDTH - 400, 100);
    	}
    }

    public void loadLevel(String level){
    	this.level = level;
    }
    
    // for loading the level
    public void run(){
    	// loading the map
    	tileMapManager.loadMap(level);
    	
    	// loading the entities
    	entityManager.addEntity(new Skeleton(new Vector2f(100, 250), new Vector2f(48, 48), Entity_ID.enemy));
    	entityManager.addEntity(new Player(new Vector2f(100, 100), new Vector2f(42, 48), Entity_ID.player));
    	
    	// initializing the chunks
    	initChunks();
    	
    	loadProgress = 10;
    }    
    
    public static Rect getChunks(){
    	return chunks;
    }
    
    public static void setPaused(boolean paused){
    	Game.paused = paused;
    }
    
    public static void nPaused(){
    	paused = !paused;
    }
    
    public void dispose(){}
}
