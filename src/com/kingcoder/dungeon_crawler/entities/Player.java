package com.kingcoder.dungeon_crawler.entities;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import com.kingcoder.dungeon_crawler.Main;
import com.kingcoder.dungeon_crawler.graphics.Animation;
import com.kingcoder.dungeon_crawler.math.Rect;
import com.kingcoder.dungeon_crawler.math.Vector2f;
import com.kingcoder.dungeon_crawler.projectiles.FireBolt;
import com.kingcoder.dungeon_crawler.projectiles.Projectile;
import com.kingcoder.dungeon_crawler.scenes.Game;


public class Player extends Entity{
    private boolean moved = false;
    private boolean melee;
    
    public Player(Vector2f spawnPos, Vector2f size, Entity_ID entity_id){
        super(spawnPos, size, entity_id, 4);
        
        // attributes
        speed = 3f;
        armor = 10;
        health = 100;
        setFireRate(5);
        melee = false;
        
        // Animation
        animations = new ArrayList<>();
        animations.add(new Animation("player/player_front_walk.anim", 5)); // walk front
        animations.add(new Animation("player/player_back_walk.anim", 5)); // walk back
        animations.add(new Animation("player/player_right_walk.anim", 5)); // walk right
        animations.add(new Animation("player/player_left_walk.anim", 5)); // walk left
        animations.get(currentAnimation).setPosition((int)position.x, (int)position.y);
        animations.get(0).setAvoidFrame(0);
        animations.get(1).setAvoidFrame(0);
        
        Main.renderer.camera.x = position.x;
        Main.renderer.camera.y = position.y;
    }

    public void update() {
    	movement();
        doLogic();
        
        // updating the animation
        animations.get(currentAnimation).setPosition((int)position.x, (int)position.y);
        animations.get(currentAnimation).update();

        // moving the camera according to player
        Main.renderer.camera.x = position.x;
        Main.renderer.camera.y = position.y;
    }

    public void render() {
    	// rendering the projectiles
    	for(int i=0; i < projectiles.size(); i++){
    		projectiles.get(i).render();
    	}
    	
    	animations.get(currentAnimation).render();
    }


    private void movement(){
        moved = false;

        if(Main.input.anyKeyPressed()) {
            direction.x = 0;
            direction.y = 0;

            if (Main.input.keyDown(KeyEvent.VK_W)) {
            	direction.y += Main.UP_DIR.y;
            }

            if (Main.input.keyDown(KeyEvent.VK_A)) {
            	direction.x += Main.LEFT_DIR.x;
            }

            if (Main.input.keyDown(KeyEvent.VK_S)) {
            	direction.y += Main.DOWN_DIR.y;
            }

            if (Main.input.keyDown(KeyEvent.VK_D)) {
            	direction.x += Main.RIGHT_DIR.x;
            }

            direction.normalize();

            if (direction.x != 0 || direction.y != 0) {
                moved = true;

                // CHECKING COLLISION & moving player
                collisionX(direction);
                collisionY(direction);
                        
                move(direction);
                
                rect.setX((int)position.x);
                rect.setY((int)position.y);
            }
        }
    }

    private void doLogic(){
    	// mouse position for logic 
    	Vector2f mousePos = Main.input.getMousePosition();
    	
    	// animation ---> setting the animations according to mouse position
    	if(mousePos.y > mousePos.x * Main.SCREEN_COEFICIENT_1 && mousePos.y > Main.HEIGHT * Main.SCALE + mousePos.x * Main.SCREEN_COEFICIENT_2){ // top triangle 
    		currentAnimation = 1;
    	}else if(mousePos.y < mousePos.x * Main.SCREEN_COEFICIENT_1 && mousePos.y < Main.HEIGHT * Main.SCALE + mousePos.x * Main.SCREEN_COEFICIENT_2){ //bottom triangle
    		currentAnimation = 0;
    	}else if(mousePos.y < mousePos.x * Main.SCREEN_COEFICIENT_1 && mousePos.y > Main.HEIGHT * Main.SCALE + mousePos.x * Main.SCREEN_COEFICIENT_2){ // right triangle
    		currentAnimation = 2;
    	}else if(mousePos.y > mousePos.x * Main.SCREEN_COEFICIENT_1 && mousePos.y < Main.HEIGHT * Main.SCALE + mousePos.x * Main.SCREEN_COEFICIENT_2){ // left triangle
    		currentAnimation = 3;
    	}
    	
    	size.x = animations.get(currentAnimation).sprite.getWidth();
		size.y = animations.get(currentAnimation).sprite.getHeight();
    	
    	if(moved){
    		if(!animations.get(currentAnimation).isStarted()){
    			animations.get(currentAnimation).start();
    		}
        }else{
        	if(animations.get(currentAnimation).isStarted()){
    			animations.get(currentAnimation).stop();
    			animations.get(currentAnimation).setCurrentFrame(0);
    		}
        	
        }
    	
    	// attack
    	if(Main.input.mouseButtonPressed(MouseEvent.BUTTON1)){
    		if(System.nanoTime() - fireTimer >= attackPerSec){
        		fireTimer = System.nanoTime();
	    		Vector2f dir = Vector2f.subtract(mousePos, Main.CENTER_OF_SCREEN);
	    		dir.normalize();
	    		attack(dir);
    		}
    	}
    	
    	for(int i=0; i < projectiles.size(); i++){
    		projectiles.get(i).update();
    		if(projectiles.get(i).isRemoved()){
    			projectiles.remove(i);
    		}
    	}
    }
    
    
    private void attack(Vector2f direction){
    	if(melee){
    		
    	}else{
    		projectiles.add(new FireBolt(new Vector2f(position.x, position.y), direction, index));
    	}
    	
    }

}
