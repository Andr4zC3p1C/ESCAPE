package com.kingcoder.dungeon_crawler.projectiles;

import com.kingcoder.dungeon_crawler.Main;
import com.kingcoder.dungeon_crawler.graphics.Particles;
import com.kingcoder.dungeon_crawler.graphics.Sprite;
import com.kingcoder.dungeon_crawler.math.Rect;
import com.kingcoder.dungeon_crawler.math.Vector2f;
import com.kingcoder.dungeon_crawler.scenes.Game;

public class Projectile {

	protected Vector2f position;
	protected Vector2f spawnPos;
	protected Vector2f direction;
	protected int speed;
	protected int damage;
	
	private boolean destroyed;
	private boolean removed;
	
	protected boolean collidedWithEntity = false;
	protected int entityIndex = 0;
	protected int ownerIndex = 0;
	protected int range;
	
	protected Sprite sprite;
	
	// particles
	protected Particles particles;
	protected int numParticles;
	protected int particleColor;
	protected long particleDurration;
	
	// TODO: add particles
	
	protected Projectile(Vector2f position, Vector2f direction, int ownerIndex){
		destroyed = false;
		removed = false;
		
		this.position = new Vector2f(position.x, position.y);
		spawnPos = new Vector2f(position.x, position.y);
		this.direction = new Vector2f(direction.x, direction.y);
		this.direction.normalize();
		
		this.ownerIndex = ownerIndex; 
	}
	
	public void update(){		
		if(!destroyed){
			// check collision for entities
			for(int i=0; i < Game.entityManager.getSize(); i++){
				if(i == ownerIndex) continue;
				
				Rect entityRect = Game.entityManager.getEntity(i).getRect();
				
				// check if inside the rectangle
				if((position.x >= entityRect.getX() - entityRect.getWidth()/2  && position.x <= entityRect.getX() + entityRect.getWidth()/2)
					&& (position.y >= entityRect.getY() - entityRect.getHeight()/2 && position.y <= entityRect.getY() + entityRect.getHeight()/2)){
					particles.setSpawnPos(position);
					destroyed = true;
					collidedWithEntity = true;
					entityIndex = i;
					break;
				}
			}
			
			// checking collision for tiles
			if(!collidedWithEntity){
				if(Game.tileMapManager.isInTileSolid((int)position.x, (int)position.y)){
					particles.setSpawnPos(position);
					destroyed = true;
				}
			}
			
			if(Vector2f.subtract(position, spawnPos).length() > range){
				particles.setSpawnPos(position);
				destroyed = true;
			}
			
			position.x += direction.x * speed;
			position.y += direction.y * speed;
			sprite.setPositionCenter(position);
			sprite.update();
		}else{
			// update the particles
			if(!particles.isRemoved()){
				particles.update();
			}else{
				removed = true;
			}
			
			if(collidedWithEntity){
				Game.entityManager.getEntity(entityIndex).reciveDamage(damage);
				collidedWithEntity = false;
			}
		}
	}
	
	protected void setParticles(int numParticles, int color, long durration){
		this.numParticles = numParticles;
		particleColor = color;
		particleDurration = durration;
	}
	
	public void render(){
		if(!destroyed){
			sprite.render();
		}else{
			particles.render();
		}
	}
	
	public boolean isRemoved(){
		return removed;
	}
}
