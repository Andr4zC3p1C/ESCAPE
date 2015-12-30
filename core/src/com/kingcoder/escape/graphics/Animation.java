package com.kingcoder.escape.graphics;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kingcoder.escape.Main;
import com.kingcoder.escape.util.ErrorLogger;

public class Animation {
	
    private int numFrames;
    private List<String> frames;
    private int currentFrame;
    private boolean started = false;

    private long timer = System.nanoTime();
    private float waitTime;

    private boolean rolling = false;
    private boolean repeating = true;

    private int avoidFrame = -1;
    
    // the sprite
    private String sprite;
    private TextureRegion region;
    private int x, y;
    private int width, height;
    
    public Animation(String path, int framesPerSec){
    	// reading from .anim file for frames 
    	try{
    		String animData = Gdx.files.internal("animations/" + path).readString();
	        String[] lines = animData.split("\n");
	        
	        frames = new ArrayList<String>();
	        for(int i=0; i < lines.length; i++){
	        	lines[i] = lines[i].trim();
	        	if(lines[i].startsWith("FPS")){ // the fps of the animation
	        		String fps = lines[i].split(" = ")[1];
	        		framesPerSec = Integer.parseInt(fps);
	        	}else if(lines[i].startsWith("repeat")){ // should the animation repeat or not
	        		String repeat = lines[i].split(" = ")[1];
	        		if(repeat.equals("true")){
	        			repeating = true;
	        			rolling = false;
	        		}else{
	        			repeating = false;
	        			rolling = true;
	        		}
	        	}else{
	        		frames.add(lines[i]);
	        	}
	    	}
    	}catch(Exception e){
    		ErrorLogger.log(e);
    	}     

        numFrames = frames.size();
        currentFrame = 0;
        waitTime = 1000000000 / framesPerSec;
        
        sprite = frames.get(0);
        region = Main.textureManager.getTextureRegion(sprite);
        width = region.getRegionWidth();
        height = region.getRegionHeight();
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
            }
        }
        
        sprite = frames.get(currentFrame);
        region = Main.textureManager.getTextureRegion(sprite);
        width = region.getRegionWidth();
        height = region.getRegionHeight();
    }

    public void render(SpriteBatch batch){
    	batch.draw(region, x - width/2, y - height / 2);
    }

    public void setCurrentFrame(int frameNum){
    	currentFrame = frameNum;
    	sprite = frames.get(frameNum);
    }
    
    public void setCurrentFrame(String frame){
    	for(int i=0; i < frames.size(); i++){
    		if(frames.get(i) == frame){
    			currentFrame = i;
    			break;
    		}
    	}
    	
    	sprite = frame;
    }
    
    public void setAvoidFrame(int frame){
    	avoidFrame = frame;
    }
    
    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
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
    
    public int getWidth(){
    	return width;
    }
    
    public int getHeight(){
    	return height;
    }
}
