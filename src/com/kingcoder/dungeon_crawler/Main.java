package com.kingcoder.dungeon_crawler;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Random;

import com.kingcoder.dungeon_crawler.graphics.Renderer;
import com.kingcoder.dungeon_crawler.graphics.TextureManager;
import com.kingcoder.dungeon_crawler.handlers.ErrorLogger;
import com.kingcoder.dungeon_crawler.handlers.Input;
import com.kingcoder.dungeon_crawler.math.Vector2f;
import com.kingcoder.dungeon_crawler.scenes.SceneManager;
import com.kingcoder.dungeon_crawler.scenes.TextureLoader;

public class Main extends Canvas implements Runnable{

    public static final int SCALE = 3;
    public static int WIDTH = 0;
    public static int HEIGHT = 0;
    public static final String VERSION = "v.0.2.1";
    public static final String TITLE = "ESCAPE!"; 
    
    // GAME LOOP
    public static boolean PRINT_FPS_UPS = true;
    private int FPS = 0;
    private int UPS = 0;
    private Font renderFont = new Font(Font.SANS_SERIF, 10,10);

    private Thread mainThread;
    private static boolean running = false;

    private BufferedImage mainImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private int[] pixels = ((DataBufferInt) mainImage.getRaster().getDataBuffer()).getData();

    // MANAGERS
    public static Random random = new Random();
    public static Input input;
    public static Renderer renderer;
    public static TextureManager textureManager;
    public static SceneManager sceneManager;

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
    
    public Main(){
        // The SIZE of our window
        setSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
        CENTER_OF_SCREEN = new Vector2f(WIDTH * SCALE /2, HEIGHT * SCALE / 2);
        SCREEN_COEFICIENT_1 = (float)HEIGHT / (float)WIDTH;
        SCREEN_COEFICIENT_2 = SCREEN_COEFICIENT_1 * -1;
    }

    public int init(){
        running = true;

        renderer = new Renderer(WIDTH, HEIGHT);
        textureManager = new TextureManager();
        sceneManager = new SceneManager();

        sceneManager.setCurrentScene(new TextureLoader());

        input = new Input();
        addKeyListener(input);
        addMouseListener(input);
        addMouseMotionListener(input);

        mainThread = new Thread(this);
        mainThread.start();
        return 0;
    }

    public void run(){
        requestFocus();

        // the game loop
        long updateTimer = System.nanoTime();
        int loops;
        int maxFrameSkip = 10;
        long skipTicks = 1000000000 / UPDATES_PER_SEC;
        long timerSec = System.currentTimeMillis();
        int frames = 0;
        int updates = 0;
        while(running){
            loops = 0;
            while(System.nanoTime() > updateTimer && loops < maxFrameSkip){
                updateTimer += skipTicks;
                update();
                updates++;
            }

            render();
            frames++;

            if(System.currentTimeMillis() - timerSec >= 1000){
                timerSec = System.currentTimeMillis();
                FPS = frames;
                UPS = updates;
                updates = 0;
                frames = 0;
            }
        }

        close();
    }


    private void update(){
        input.update();

        if(sceneManager.getCurrentScene().isInitialized()) {
            sceneManager.getCurrentScene().update();
        }
    }

    private void render(){
        if(getBufferStrategy() == null){
            createBufferStrategy(3);
        }

        Graphics g = getBufferStrategy().getDrawGraphics();
        renderer.clear();

        // rendering with pixel renderer
        if(sceneManager.getCurrentScene().isInitialized()) {
            sceneManager.getCurrentScene().render();
        }

        for(int i=0; i<pixels.length; i++) {
            pixels[i] = renderer.pixels[i];
        }
        
        g.drawImage(mainImage, 0,0, WIDTH * SCALE, HEIGHT * SCALE, null);

        // rendering with high_res renderer
        if(sceneManager.getCurrentScene().isInitialized()) {
            sceneManager.getCurrentScene().renderHighRes(g);
        }
        
        if(PRINT_FPS_UPS){
            g.setColor(Color.WHITE);
            g.setFont(renderFont);
            g.drawString("FPS: " + String.valueOf(FPS), 5, 10);
            g.drawString("UPS: " + String.valueOf(UPS), 5, 20);
        }

        g.dispose();
        getBufferStrategy().show();
    }


    private void close(){
        // disposing
        sceneManager.getCurrentScene().dispose();
        textureManager.clear();

        // closing the threads
        try {
            mainThread.join();
        }catch (Exception e){
        	ErrorLogger.log(e);
        }
        System.exit(0);
    }

    public static void setRunning(boolean _running){
        running = _running;
    }

    public static void main(String[] args){
        new Window();
    }
}
