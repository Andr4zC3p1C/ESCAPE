package com.kingcoder.escape.entities;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kingcoder.escape.Main;
import com.kingcoder.escape.graphics.Animation;
import com.kingcoder.escape.math.Vector2f;
import com.kingcoder.escape.projectiles.FireBolt;
import com.kingcoder.escape.scenes.Game;


public class Player extends Entity{
    private boolean moved = false;
    private boolean melee;
    
    public Player(Vector2f spawnPos, Vector2f size, Entity_ID entity_id){
        super(spawnPos, size, entity_id, 4);
        
        // attributes
        speed = 4f;
        armor = 10;
        health = 100;
        setFireRate(5);
        melee = false;
        
        // Animation
        animations = new ArrayList<Animation>();
        animations.add(new Animation("player/player_front_walk.anim", 0)); // walk front
        animations.add(new Animation("player/player_back_walk.anim", 0)); // walk back
        animations.add(new Animation("player/player_right_walk.anim", 0)); // walk right
        animations.add(new Animation("player/player_left_walk.anim", 0)); // walk left
        animations.get(currentAnimation).setPosition((int)position.x, (int)position.y);
        animations.get(0).setAvoidFrame(0);
        animations.get(1).setAvoidFrame(0);
    }

    public void updateA() {
    	movement();
        doLogic();
        
        // updating the animation
        animations.get(currentAnimation).setPosition((int)position.x, (int)position.y);
        animations.get(currentAnimation).update();

        // updating the camera position
        Main.renderer.setCameraPosition(position);
    }

    public void renderA(SpriteBatch batch) {
    	// rendering the projectiles
    	for(int i=0; i < projectiles.size(); i++){
    		projectiles.get(i).render(batch);
    	}

    	animations.get(currentAnimation).render(batch);
    }


    private void movement(){
        moved = false;

        if(Gdx.input.isKeyPressed(Keys.ANY_KEY)) {
            direction.x = 0;
            direction.y = 0;

            if (Gdx.input.isKeyPressed(Keys.W)) {
            	direction.y += 1;
            }

            if (Gdx.input.isKeyPressed(Keys.A)) {
            	direction.x += -1;
            }

            if (Gdx.input.isKeyPressed(Keys.S)) {
            	direction.y += -1;
            }

            if (Gdx.input.isKeyPressed(Keys.D)) {
            	direction.x += 1;
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
    	
    	// animation ---> setting the animations according to mouse position
    	if(Game.mousePos.y > Game.mousePos.x * Main.SCREEN_COEFICIENT_1 && Game.mousePos.y > Main.HEIGHT + Game.mousePos.x * Main.SCREEN_COEFICIENT_2){ // top triangle 
    		currentAnimation = 1;
    	}else if(Game.mousePos.y < Game.mousePos.x * Main.SCREEN_COEFICIENT_1 && Game.mousePos.y < Main.HEIGHT + Game.mousePos.x * Main.SCREEN_COEFICIENT_2){ //bottom triangle
    		currentAnimation = 0;
    	}else if(Game.mousePos.y < Game.mousePos.x * Main.SCREEN_COEFICIENT_1 && Game.mousePos.y > Main.HEIGHT + Game.mousePos.x * Main.SCREEN_COEFICIENT_2){ // right triangle
    		currentAnimation = 2;
    	}else if(Game.mousePos.y > Game.mousePos.x * Main.SCREEN_COEFICIENT_1 && Game.mousePos.y < Main.HEIGHT + Game.mousePos.x * Main.SCREEN_COEFICIENT_2){ // left triangle
    		currentAnimation = 3;
    	}
    	
    	size.x = animations.get(currentAnimation).getWidth();
		size.y = animations.get(currentAnimation).getHeight();
    	
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
    	if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
    		if(System.nanoTime() - fireTimer >= attackPerSec){
        		fireTimer = System.nanoTime();
	    		Vector2f dir = Vector2f.subtract(Game.mousePos, Main.CENTER_OF_SCREEN);
	    		dir.normalize();
	    		attack(dir);
    		}
    	}
    	
    	// updating all the projectiles
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
