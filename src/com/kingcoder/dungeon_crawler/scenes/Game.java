package com.kingcoder.dungeon_crawler.scenes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.kingcoder.dungeon_crawler.Main;
import com.kingcoder.dungeon_crawler.entities.Entity_ID;
import com.kingcoder.dungeon_crawler.entities.Player;
import com.kingcoder.dungeon_crawler.entities.Skeleton;
import com.kingcoder.dungeon_crawler.game_managers.EntityManager;
import com.kingcoder.dungeon_crawler.game_managers.HUDManager;
import com.kingcoder.dungeon_crawler.game_managers.MapObjectManager;
import com.kingcoder.dungeon_crawler.game_managers.TileMapManager;
import com.kingcoder.dungeon_crawler.game_managers.UIManager;
import com.kingcoder.dungeon_crawler.handlers.ErrorLogger;
import com.kingcoder.dungeon_crawler.math.Vector2f;

public class Game extends Scene implements Runnable{
    public static TileMapManager tileMapManager;
    public static MapObjectManager mapObjectManager;
    public static EntityManager entityManager;
    public static HUDManager hudManager;
    public static UIManager uiManager;

    // level to be loaded
    private String level;
    
    // lighting shader
    private BufferedImage lightingShader;
    private int shaderX, shaderY, shaderWidth, shaderHeight;
    private Color shaderColor = new Color(0,0,0,200);
    
    // loading
    private Thread loadThread;
    private int loadProgress = -1;

    public void init(){
        sceneID = SceneID.Game;
        
        // test level
        level = "test_level";

        tileMapManager = new TileMapManager();
        mapObjectManager = new MapObjectManager();
        entityManager = new EntityManager();
        hudManager = new HUDManager();
        uiManager = new UIManager();

        loadThread = new Thread(this);

        try {
			lightingShader = ImageIO.read(getClass().getResourceAsStream("/textures/shader/high_res_shader.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        shaderWidth = 800; 
        shaderHeight = 800;
        shaderX = (int)(Main.WIDTH * Main.SCALE / 2) - shaderWidth/2;
        shaderY = (int)(Main.HEIGHT * Main.SCALE / 2) - shaderHeight/2;
        
        initialized = true;
    }

    public void update(){
        if(loadProgress == 1){
        	tileMapManager.update();
        	mapObjectManager.update();
        	entityManager.update();
        	hudManager.update();
        	uiManager.update();
        }else if(loadProgress == -1){
        	loadProgress = 0;
            loadThread.start();
        }else if(loadProgress == 10){
        	loadProgress = 1;
        	
        	try {
				loadThread.join();
			} catch (InterruptedException e) {
				ErrorLogger.log(e);
			}
        }
    }

    public void render(){
    	if(loadProgress == 1){
        	tileMapManager.render();
        	mapObjectManager.render();
        	entityManager.render();
        }
    }
    
    public void renderHighRes(Graphics g){
    	if(loadProgress == 1){
    		renderLightingShader(g);
    		
        	hudManager.render(g);
        	uiManager.render(g);
        }
    }

    public void dispose(){

    }

    public void loadLevel(String level){
    	this.level = level;
    }
    
    
    // for loading
    public void run(){
    	tileMapManager.loadMap(level);
    	Player player = new Player(new Vector2f(100, 100), new Vector2f(21, 24), Entity_ID.player);
    	
    	entityManager.addEntity(new Skeleton(new Vector2f(100, 250), new Vector2f(16, 16), Entity_ID.basic_enemy));
    	entityManager.addEntity(player);
    	
    	loadProgress = 10;
    }

    
    /*
     *  OTHER FUNCTIONS
     * 	
     * */
    private void renderLightingShader(Graphics g){
    	// lighting shader
		g.drawImage(lightingShader, shaderX, shaderY, shaderWidth, shaderHeight, null);
		g.setColor(shaderColor);
		g.fillRect(0, 0, (int)(Main.WIDTH * Main.SCALE), (int) (Main.HEIGHT * Main.SCALE / 2 - shaderHeight / 2)); // top rectangle
		g.fillRect(0, (int) (Main.HEIGHT * Main.SCALE / 2 + shaderHeight / 2), (int)(Main.WIDTH * Main.SCALE), (int) (Main.HEIGHT * Main.SCALE / 2 - shaderHeight / 2)); // bottom rectangle
		g.fillRect(0, (int) (Main.HEIGHT * Main.SCALE / 2 - shaderHeight / 2), (int)(Main.WIDTH * Main.SCALE / 2 - shaderWidth/2), shaderHeight); // left rectangle
		g.fillRect((int)(Main.WIDTH * Main.SCALE / 2 + shaderWidth/2), (int) (Main.HEIGHT * Main.SCALE / 2 - shaderHeight / 2), (int)(Main.WIDTH * Main.SCALE / 2 - shaderWidth/2), shaderHeight); // right rectangle
    }
    
    
    
}
