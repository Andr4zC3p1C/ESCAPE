package com.kingcoder.escape;

import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.kingcoder.escape.graphics.Renderer;
import com.kingcoder.escape.graphics.TextureManager;
import com.kingcoder.escape.math.Vector2f;
import com.kingcoder.escape.scenes.SceneManager;
import com.kingcoder.escape.scenes.TextureLoader;

public class Main extends ApplicationAdapter {

	// properties variables
    public static float WIDTH = 0;
    public static float HEIGHT = 0;
    public static final String VERSION = "v.0.3.1";
    public static final String TITLE = "ESCAPE!"; 
	
	// managers
    public static Renderer renderer;
    public static Random random;
	public static SceneManager sceneManager;
    public static TextureManager textureManager;
	
    // OTHER VARIABLES
    public static final int UPDATES_PER_SEC = 60;
    public static final long NANO_SECOND = 1000000000; 
    public static Vector2f CENTER_OF_SCREEN;
    public static final Vector2f RIGHT_DIR = new Vector2f(1, 0);
    public static final Vector2f LEFT_DIR = new Vector2f(-1, 0);
    public static final Vector2f UP_DIR = new Vector2f(0, 1);
    public static final Vector2f DOWN_DIR = new Vector2f(0, -1);
    public static float SCREEN_COEFICIENT_1;
    public static float SCREEN_COEFICIENT_2;
    
    @Override
	public void create () {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		
        CENTER_OF_SCREEN = new Vector2f(WIDTH / 2, HEIGHT / 2);
        SCREEN_COEFICIENT_1 = HEIGHT / WIDTH;
        SCREEN_COEFICIENT_2 = SCREEN_COEFICIENT_1 * -1;
        
        renderer = new Renderer();
        random = new Random();
		sceneManager = new SceneManager();
		textureManager = new TextureManager();
		
		sceneManager.setCurrentScene(new TextureLoader());
	}
	
	// game loop variables
	private static long updateTimer = System.nanoTime();
	private static int loops;
	private static int maxFrameSkip = 10;
	private static long skipTicks = 1000000000 / UPDATES_PER_SEC;
	
	// FPS limiting
	private static long renderTimer = System.nanoTime();
	private static final long FPS_LIMIT = 1000000000 / 250;
	
	private static long timerSec = System.currentTimeMillis();
	private static int frames = 0;
	private static int updates = 0;
	
	// for rendering
	public static int FPS = 0;
	public static int UPS = 0;
    public static boolean PRINT_FPS_UPS = true;
    
	public void render () {
		loops = 0;
        while(System.nanoTime() > updateTimer && loops < maxFrameSkip){
            updateTimer += skipTicks;
            update();
            updates++;
        }

    	renderer.render();
    	frames++;

        if(System.currentTimeMillis() - timerSec >= 1000){
            timerSec = System.currentTimeMillis();
            FPS = frames;
            UPS = updates;
            updates = 0;
            frames = 0;
        }
	}
	
	private void update(){
		if(sceneManager.getCurrentScene().isInitialized()) {
			sceneManager.getCurrentScene().update();
		}
	}
	
	@Override
	public void resize(int width, int height){
		
	}

	@Override
	public void dispose(){
		renderer.dispose();
	}
}
