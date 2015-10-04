package com.kingcoder.dungeon_crawler.graphics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import com.kingcoder.dungeon_crawler.Main;
import com.kingcoder.dungeon_crawler.handlers.ErrorLogger;

public class Animation {
    public Sprite sprite;
    private int numFrames;
    private String[] frames;
    private int currentFrame;
    private boolean started = false;

    private long timer = System.nanoTime();
    private float waitTime;

    private boolean rolling = false;
    private boolean repeating = true;

    private int avoidFrame = -1;
    
    public Animation(String path, int framesPerSec){
    	// reading from .anim file for frames 
    	try{
	    	InputStreamReader isr = new InputStreamReader(getClass().getResourceAsStream("/animations/" + path));
	        BufferedReader br = new BufferedReader(isr);

	        int numLines = 0;
	        Vector<String> lines = new Vector<>();
	        while (true){
	        	String line = br.readLine();
	        	if(line == null) break;
	        	lines.add(line);
	        	numLines++;
	        }
	       
	        frames = new String[numLines];
	        for(int i=0; i < numLines; i++){
	    		frames[i] = lines.get(i);
	    	}
	        
	        lines.clear();
	        br.close();
    	}catch(IOException e){
    		ErrorLogger.log(e);
    	}
        
        Texture tex = Main.textureManager.getTexture(frames[0]);
        sprite = new Sprite(tex, tex.getWidth(), tex.getHeight());

        numFrames = frames.length;
        currentFrame = 0;
        waitTime = 1000000000 / framesPerSec;
    }

    public void update(){
        if(started){
            if (System.nanoTime() - timer >= waitTime) {
                timer = System.nanoTime();
                if (repeating) {
                    if (currentFrame < numFrames) {
                        currentFrame++;
                    }
                }else if (rolling) {
                    if (currentFrame < numFrames) {
                        currentFrame++;
                    } else {
                        stop();
                    }
                }
               
                if(currentFrame >= numFrames){
            		currentFrame = 0;
            	}
                
                if(currentFrame == avoidFrame){
                	currentFrame++;
                }
                
                sprite.setTexture(Main.textureManager.getTexture(frames[currentFrame]));
            }
        }

        sprite.update();
    }

    public void render(){
        sprite.render();
    }

    public void setCurrentFrame(int frameNum){
    	currentFrame = frameNum;
    	sprite.setTexture(Main.textureManager.getTexture(frames[currentFrame]));
    }
    
    public void setCurrentFrame(String frame){
    	for(int i=0; i < frames.length; i++){
    		if(frames[i] == frame){
    			currentFrame = i;
    			break;
    		}
    	}
    	sprite.setTexture(Main.textureManager.getTexture(frames[currentFrame]));
    }
    
    public void setAvoidFrame(int frame){
    	avoidFrame = frame;
    }
    
    public void setPosition(int x, int y){
        sprite.setPositionCenter(x, y);
    }

    public void setRate(int framesPerSec){
        waitTime = 1000000000 / framesPerSec;
    }

    public void start(){
        currentFrame = 0;
        started = true;
    }

    public void goOn(){
        started = true;
    }

    public void stop(){
        started = false;
    }

    public void roll(){
        rolling = true;
        repeating = false;
        start();
    }

    public void repeat(){
        rolling = false;
        repeating = true;
        start();
    }

    public boolean isStarted(){
    	return started;
    }
    
    public boolean isRolling(){
    	return rolling;
    }
    
    public boolean isRepeating(){
    	return repeating;
    }
}
