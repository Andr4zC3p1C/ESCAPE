package com.kingcoder.escape.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kingcoder.escape.Main;
import com.kingcoder.escape.math.Vector2f;
import com.kingcoder.escape.projectiles.FireBolt;

public class Skeleton extends Enemy{
	
	public static final int FIRE_RATE = 2;
	
	// TODO: add animations
	
	public Skeleton(Vector2f spawnPos, Vector2f size, Entity_ID entity_id) {
		super(spawnPos, size, entity_id, FIRE_RATE);
		
		// Attributes
		health = 300;
		speed = 2;
		armor = 10;
		rangeOfAttack = 150;
		minRangeOfAttack = 200;
		rangeOfSight = 800; 
		alarmRange = 1000;
		
		// animations
		/*
		animations = new ArrayList<Animation>();
		animations.add(new Animation("", 3)); // front
		animations.add(new Animation("", 3)); // right
		animations.add(new Animation("", 3)); // left
		animations.add(new Animation("", 3)); // back
		*/
	}
	
	public void updateEnemy(){
		doLogic();
		rect.setX((int)position.x);
		rect.setY((int)position.y);
	}
	
	public void renderEnemy(SpriteBatch batch) {
		// rendering the projectiles
    	for(int i=0; i < projectiles.size(); i++){
    		projectiles.get(i).render(batch);
    	}
    	
    	if(!dead){
    		TextureRegion region = Main.textureManager.getTextureRegion("stone_floor");
    		batch.draw(region, position.x - size.x/2, position.y - size.y/2);
    	}
	}

	public void attack(){
		// attack speed control
		attacking = true;
		if(System.nanoTime() - fireTimer >= attackPerSec){
			fireTimer = System.nanoTime();
			
			// do the attack :
			projectiles.add(new FireBolt(position, direction, index));
			
			attacking = false;
		}
	}
	
	private void doLogic(){
		// updating the projectiles
		for(int i=0; i < projectiles.size(); i++){
    		projectiles.get(i).update();
    		if(projectiles.get(i).isRemoved()){
    			projectiles.remove(i);
    		}
    	}
		
		if(dead){
			die();
		}
	}
	
	public void die() {
		// TODO: add death roll animation
		if(projectiles.size() == 0){
			removed = true;
		}
	}
}
