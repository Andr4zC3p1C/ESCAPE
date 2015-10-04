package com.kingcoder.dungeon_crawler.entities;

import com.kingcoder.dungeon_crawler.graphics.Sprite;
import com.kingcoder.dungeon_crawler.graphics.Texture;
import com.kingcoder.dungeon_crawler.math.Vector2f;
import com.kingcoder.dungeon_crawler.projectiles.FireBolt;

public class Skeleton extends BasicEnemy{
	
	public static final int FIRE_RATE = 2;
	
	// TODO: add animations, 
	Sprite sprite;
	
	public Skeleton(Vector2f spawnPos, Vector2f size, Entity_ID entity_id) {
		super(spawnPos, size, entity_id, FIRE_RATE);
		
		// Attributes
		health = 300;
		speed = 2f;
		armor = 10;
		rangeOfAttack = 100;
		minRangeOfAttack = 120;
		rangeOfSight = 200; 
		alarmRange = 1000;
		
		// animations
		/*
		animations = new ArrayList<>();
		animations.add(new Animation("", 3)); // front
		animations.add(new Animation("", 3)); // right
		animations.add(new Animation("", 3)); // left
		animations.add(new Animation("", 3)); // back
		*/
		
		int[] pixels = new int[(int) (size.x * size.y)];
		for(int i = 0; i < pixels.length; i++){
			pixels[i] = 0xffffff;
		}
		Texture tex = new Texture(pixels, (int)size.x, (int)size.y);
		sprite = new Sprite(tex, (int)size.x, (int)size.y);
		sprite.setPositionCenter(position);
	}

	public void updateEnemy(){
		doLogic();
		rect.setX((int)position.x);
		rect.setY((int)position.y);
		sprite.setPositionCenter(position);
		sprite.update();
	}
	
	public void renderEnemy() {
		// rendering the projectiles
    	for(int i=0; i < projectiles.size(); i++){
    		projectiles.get(i).render();
    	}
    	
    	if(!dead){
    		sprite.render();
    	}
	}

	public void attack(){
		// attack speed control
		if(System.nanoTime() - fireTimer >= attackPerSec){
			fireTimer = System.nanoTime();
			projectiles.add(new FireBolt(position, direction, index));
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
