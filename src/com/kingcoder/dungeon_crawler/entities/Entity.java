package com.kingcoder.dungeon_crawler.entities;

import java.util.ArrayList;

import com.kingcoder.dungeon_crawler.graphics.Animation;
import com.kingcoder.dungeon_crawler.math.Rect;
import com.kingcoder.dungeon_crawler.math.Vector2f;
import com.kingcoder.dungeon_crawler.projectiles.Projectile;
import com.kingcoder.dungeon_crawler.scenes.Game;

public abstract class Entity {
    protected int index;
    protected Entity_ID entity_id;
    
    protected Vector2f position;
    protected Vector2f direction;
    protected Vector2f spawnPos;
    protected Vector2f size;
    protected float speed;
    protected boolean removed = false;
    
    // Attributes
    protected int health;
    protected int armor;
    protected boolean dead;
    protected String equipedWeapon;
    
    // for collision detection
    protected Rect rect;
    protected boolean[] collisions;
    protected int collisionPointsX;
    protected int collisionPointsY;
    protected int distBetPointsX;
    protected int distBetPointsY;
    
    // animation
    protected ArrayList<Animation> animations;
    protected int currentAnimation = 0;
    
    // attack
    protected ArrayList<Projectile> projectiles;
    protected long fireTimer = System.nanoTime();
    protected float attackPerSec;

    
    public Entity(Vector2f spawnPos, Vector2f size, Entity_ID entity_id, int fireRate){
    	this.entity_id = entity_id;
    	
    	this.size = size;
    	this.spawnPos = spawnPos;
        position = new Vector2f(spawnPos.x, spawnPos.y);
        direction = new Vector2f(0,0);
        rect = new Rect((int)spawnPos.x, (int)spawnPos.y, (int)size.x, (int)size.y);
        
        collisionPointsX = 2 + (int)size.x / Game.tileMapManager.tileMap.getTileSize();
        collisionPointsY = 2 + (int)size.y / Game.tileMapManager.tileMap.getTileSize();
        distBetPointsX = (int)size.x / collisionPointsX;
        distBetPointsY = (int)size.y / collisionPointsY;
        collisions = new boolean[4];
                
        attackPerSec = 1000000000 / fireRate;
        
        projectiles = new ArrayList<>();
    }
    
    
    protected void remove(){
        removed = true;
    }

    public boolean isRemoved(){
        return removed;
    }

    public abstract void update();
    public abstract void render();
    
    // FUNCTIONS
    protected void collisionX(Vector2f dir){
    	dir.normalize();
        collisionPointsY = 2 + (int)size.y / Game.tileMapManager.tileMap.getTileSize();
    	
        int tx = (int)(position.x + dir.x * speed);
        int ty = (int)position.y;
        
        // right
        if(dir.x > 0){
        	for(int i=0; i < collisionPointsY + 1; i++){
        		if(i == collisionPointsY){
        			if(Game.tileMapManager.isInTileSolid(tx + (int)size.x/2, ty - (int)size.y/2 + (int)size.y)){
            			collisions[0] = true;
            			return;
            		}
        		}else if(Game.tileMapManager.isInTileSolid(tx + (int)size.x/2, ty - (int)size.y/2 + distBetPointsY * i)){
        			collisions[0] = true;
        			return;
        		}
        	}
        	collisions[0] = false;
        }else if(dir.x < 0){ // left
        	for(int i=0; i < collisionPointsY + 1; i++){
        		if(i == collisionPointsY){
        			if(Game.tileMapManager.isInTileSolid(tx - (int)size.x/2, ty - (int)size.y/2 + (int)size.y)){
            			collisions[0] = true;
            			return;
            		}
        		}else if(Game.tileMapManager.isInTileSolid(tx - (int)size.x/2, ty - (int)size.y/2 + distBetPointsY * i)){
        			collisions[1] = true;
        			return;
        		}
        	}
        	collisions[1] = false;           
        }
    }

    protected void collisionY(Vector2f dir){
    	dir.normalize();
    	collisionPointsX = 2 + (int)size.x / Game.tileMapManager.tileMap.getTileSize();
    	
        int tx = (int)position.x;
        int ty = (int)(position.y + dir.y * speed);

        // top
        if(dir.y > 0){
        	for(int i=0; i < collisionPointsX + 1; i++){
        		if(i == collisionPointsX){
        			if(Game.tileMapManager.isInTileSolid(tx - (int)size.x/2 + (int)size.x, ty + (int)size.y/2)){
            			collisions[0] = true;
            			return;
            		}
        		}else if(Game.tileMapManager.isInTileSolid(tx - (int)size.x/2 + distBetPointsX * i, ty + (int)size.y/2)){
        			collisions[2] = true;
        			return;
        		}
        	}
        	collisions[2] = false;            
        }else if(dir.y < 0){ // bottom
        	for(int i=0; i < collisionPointsX + 1; i++){
        		if(i == collisionPointsX){
        			if(Game.tileMapManager.isInTileSolid(tx - (int)size.x/2 + (int)size.x, ty - (int)size.y/2)){
            			collisions[0] = true;
            			return;
            		}
        		}else if(Game.tileMapManager.isInTileSolid(tx - (int)size.x/2 + distBetPointsX * i, ty - (int)size.y/2)){
        			collisions[3] = true;
        			return;
        		}
        	}
        	collisions[3] = false;
        }
    }
    
	protected void move(Vector2f direction){
		direction.normalize();
		
		int tileSize = Game.tileMapManager.tileMap.getTileSize();
        int tx = (int)(position.x + direction.x * speed);
        int ty = (int)(position.y + direction.y * speed);
        
        // X
        if(direction.x > 0){ // right
        	if(collisions[0]){
        		position.x = ((tx + (int)size.x/2) / tileSize) * tileSize - size.x/2 - 1;
        	}else{
            	position.x += direction.x * speed;
            }
        }else if(direction.x < 0){ // left
        	if(collisions[1]){
        		position.x = ((tx - (int)size.x/2) / tileSize) * tileSize + tileSize + size.x/2 + 1;
        	}else{
            	position.x += direction.x * speed;
            }
        }
        
        // Y
        if(direction.y > 0){ // top
        	if(collisions[2]){
        		position.y = ((ty + (int)size.y/2) / tileSize) * tileSize - size.y/2 - 1;
        	}else{
        		position.y += direction.y * speed;
            }
        }else if(direction.y < 0){ // bottom
        	if(collisions[3]){
        		position.y = ((ty - (int)size.y/2) / tileSize) * tileSize + tileSize + size.y/2 + 1;
        	}else{
            	position.y += direction.y * speed;
            }
        }
	}
    
    public void reciveDamage(int damage){
    	damage -= armor; 
    	if(health - damage < 0){
    		health = 0;
    	}else{
    		health -= damage;
    	}
    }

    // setters
    public void setIndex(int index){
        this.index = index;
    }

    public void setPosition(Vector2f position){
        this.position = position;
    }

    public void setSize(Vector2f size){
        this.size = size;
    }

    public void setFireRate(int fireRate){
    	attackPerSec = 1000000000 / fireRate;
    }
    
    // getters
    public Entity_ID getEntity_ID(){
    	return entity_id;
    }
    
    public Vector2f getPosition(){
        return position;
    }

    public Vector2f getSize(){
        return size;
    }

    public int getIndex(){
        return index;
    }

    public Rect getRect(){
        return rect;
    }

}
