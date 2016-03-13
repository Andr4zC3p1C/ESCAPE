package com.kingcoder.escape.entities;

import java.util.Stack;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kingcoder.escape.Main;
import com.kingcoder.escape.math.Vector2f;
import com.kingcoder.escape.scenes.Game;

public abstract class Enemy extends Entity{

	// TODO: A* PATH FINDING ALGORITHM
	
	// constants
	public static final int MAX_DISTANCE = 100;
	
	// states
	public static final int IDLE = 0;
	public static final int ALERT = 1;
	public static final int AGGRESSIVE = 2;
	public static final int MOVING_THE_PATH = 3;
	
	// attributes
	protected int state = IDLE;
	protected float distanceTraveled = 0;
	protected Vector2f ray; 
	protected boolean playerSpotted = false;
	protected boolean activated = false;
	protected boolean attacking = false;
	
	protected int rangeOfSight = 0;
	protected int minRangeOfAttack = 0;
	protected int rangeOfAttack = 0;
	protected int alarmRange = 0;
	
	// wait timer 
	protected long waitTimer = System.nanoTime();
	protected long waitTime = (long)(0.5f * Main.NANO_SECOND);
	
	// A* algorithm path attributes
	private Stack<Vector2f> pathDirs; // normalized vectors representing the direction to travel
	private Stack<Integer> pathLengths; // the lengths of the path in number of updates
	private int pathCounter; // total counter of updates
	
	public Enemy(Vector2f spawnPos, Vector2f size, Entity_ID entity_id, int fireRate) {
		super(spawnPos, size, entity_id, fireRate);
		
		ray = new Vector2f(spawnPos.x, spawnPos.y);
	}

	public void updateA() {
		// check if visible(if it is do AI stuff)
		
		if(!dead){
			if(activated){
				System.out.println(state);
				AI();
			}else {
				// TODO : check if inside of loaded chunks
				activated = true;
			}
		}
		
		updateEnemy();
	}

	public abstract void updateEnemy();
	
	public void renderA(SpriteBatch batch) {
		renderEnemy(batch);
	}

	public abstract void renderEnemy(SpriteBatch batch);
	
	private void AI(){
		// the player
		float playerX = Game.entityManager.getEntity(Entity_ID.player).getPosition().x;
		float playerY = Game.entityManager.getEntity(Entity_ID.player).getPosition().y;
		
		// check if dead
		if(health <= 0){
			dead = true;
			return;
		}		
		
		// collision detection
		collisionX(direction);
		collisionY(direction);
		
		boolean playerInSight = raycastAPoint(playerX, playerY);
		
		switch(state){
		case IDLE: // random movement & checking for player(raycasting --> seeing the player)
			// the movement ---> if not colliding with anything
			
			if(System.nanoTime() - waitTimer > waitTime){
				move(direction);
	            
	            distanceTraveled += speed;
			}
			
			if(collisions[0] || collisions[1] || collisions[2] || collisions[3] || distanceTraveled >= MAX_DISTANCE){ 
				int dir = 0;
				distanceTraveled = 0;
				waitTimer = System.nanoTime();
				
				for(int i=0; i < 4; i++){
					if(collisions[i]){
						dir = i;
						while(dir == i){
							dir = Main.random.nextInt(40) / 10;
						}
						collisions[i] = false;
						break;
					}else{
						dir = Main.random.nextInt(40) / 10;
					}
				}
				
				switch(dir){
				case 0: // right
					direction.x = 1;
					direction.y = 0;
					break;
				case 1: // left
					direction.x = -1;
					direction.y = 0;
					break;
				case 2: // up
					direction.x = 0;
					direction.y = 1;
					break;
				case 3: // down
					direction.x = 0;
					direction.y = -1;
					break;
				}
				
				direction.normalize();
			}
			
			
			if(!playerSpotted){
				if(playerInSight){
					state = ALERT;
					playerSpotted = true;
					activated = true;
					
					// alerting other enemies
					for(int i=0; i < Game.entityManager.getSize(); i++){
						// check if basic enemy
						if(Game.entityManager.getEntity(i).getEntity_ID() == Entity_ID.enemy){
							// check if near
							Enemy enemy = (Enemy) Game.entityManager.getEntity(i);
							if(enemy.position.x > position.x - alarmRange/2 && enemy.position.x < position.x + alarmRange/2){
								if(enemy.position.y > position.y - alarmRange/2 && enemy.position.y < position.y + alarmRange/2){
									enemy.playerSpotted = true;
									enemy.activated = true;
								}
							}
						}
					}
				}
				
			}else{
				state = ALERT;
			}
			
			break;
			
		case ALERT: // move into range of attack
			direction.x = playerX - position.x;
			direction.y = playerY - position.y;
			
			if(playerInSight){
				if(direction.length() <= minRangeOfAttack){
					state = AGGRESSIVE;
				}else{
					move(direction);
				}
			}else{
				findPath();
				state = MOVING_THE_PATH;
				playerSpotted = false;
			}
			
			break;
			
		case AGGRESSIVE: // attack the player
			direction.x = playerX - position.x;
			direction.y = playerY - position.y;
			attack();
			
			// If attacking don't do anything else
			if(!attacking){
				if(playerInSight){
					if(direction.length() >= minRangeOfAttack){
						state = ALERT;
					}else if(direction.length() >= rangeOfAttack){
						move(direction);
						System.out.println();
					}
				}else{
					findPath();
					state = MOVING_THE_PATH;
					playerSpotted = false;
				}
			}
			
			break;
			
		case MOVING_THE_PATH:
			/*
			// checking if the player is in sight
			if(!playerInSight){
				// checking if current path has been reached
				if(pathCounter == pathLengths.lastElement()){
					pathLengths.pop();
					pathDirs.pop();
					pathCounter = 0;
				}
				
				// moving in the current direction
				move(pathDirs.lastElement());
				
				pathCounter++;
				
				if(pathDirs.empty()){
					state = IDLE;
				}
			}else{
				state = ALERT;
			}
			*/
			state = IDLE;
			break;
		}
	}
	
	private void findPath(){
		// the A* algorithm
		
	}
	
	private boolean raycastAPoint(float x, float y){
		boolean seen = false;		
		
		Vector2f rayDir = new Vector2f(0,0);
		rayDir.x = x - position.x;
		rayDir.y = y - position.y;
		
		if(rayDir.length() < rangeOfSight){
			rayDir.normalize();
			seen = true;
			
			ray.x = position.x;
			ray.y = position.y;
			
			while((int)ray.x != (int)x && (int)ray.y != (int)y){
				ray.x += rayDir.x;
				ray.y += rayDir.y;
				
				if(Game.tileMapManager.isInTileSolid((int)ray.x, (int)ray.y)){
					seen = false;
					break;
				}
			}
		}else{
			seen = false;
		}
		
		return seen;
	}
	
	public abstract void attack();
	
	public abstract void die();
}
