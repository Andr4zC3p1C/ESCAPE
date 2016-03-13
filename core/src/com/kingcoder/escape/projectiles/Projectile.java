package com.kingcoder.escape.projectiles;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kingcoder.escape.Main;
import com.kingcoder.escape.graphics.Particles;
import com.kingcoder.escape.math.Rect;
import com.kingcoder.escape.math.Vector2f;
import com.kingcoder.escape.scenes.Game;

public class Projectile {

	protected String sprite;
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
	
	// particles
	protected Particles destroyParticles;
	private List<Particles> airParticles; 
	
	protected Projectile(Vector2f position, Vector2f direction, int ownerIndex, String sprite){
		destroyed = false;
		removed = false;
		
		this.position = new Vector2f(position.x, position.y);
		spawnPos = new Vector2f(position.x, position.y);
		this.direction = new Vector2f(direction.x, direction.y);
		this.direction.normalize();
		
		this.ownerIndex = ownerIndex; 
		
		this.sprite = sprite;
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
					destroyParticles.setSpawnPos(position);
					destroyed = true;
					collidedWithEntity = true;
					entityIndex = i;
					break;
				}
			}
			
			// checking collision for tiles
			if(!collidedWithEntity){
				if(Game.tileMapManager.isInTileSolid((int)position.x, (int)position.y)){
					destroyParticles.setSpawnPos(position);
					destroyed = true;
				}
			}
			
			if(Vector2f.subtract(position, spawnPos).length() > range){
				destroyParticles.setSpawnPos(position);
				destroyed = true;
			}
			
			position.x += direction.x * speed;
			position.y += direction.y * speed;
		}else{
			// update the particles
			if(!destroyParticles.isRemoved()){
				destroyParticles.update();
			}else{
				destroyParticles.dispose();
				removed = true;
			}

			if(collidedWithEntity){
				Game.entityManager.getEntity(entityIndex).reciveDamage(damage);
				collidedWithEntity = false;
			}
		}
	}
	
	public void render(SpriteBatch batch){
		if(!destroyed){
			Sprite projectile = Main.textureManager.getSprite(sprite);
			projectile.setCenter(position.x, position.y);
			projectile.draw(batch);
		}else{
			destroyParticles.render(batch);
		}
	}
	
	public boolean isRemoved(){
		return removed;
	}
}
