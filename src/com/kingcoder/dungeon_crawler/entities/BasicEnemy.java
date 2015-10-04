package com.kingcoder.dungeon_crawler.entities;

import com.kingcoder.dungeon_crawler.Main;
import com.kingcoder.dungeon_crawler.math.Rect;
import com.kingcoder.dungeon_crawler.math.Vector2f;
import com.kingcoder.dungeon_crawler.scenes.Game;

public abstract class BasicEnemy extends Entity{

	// TODO: PATH FINDING ALGORITHM
	
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
	
	protected int rangeOfSight = 0;
	protected int minRangeOfAttack = 0;
	protected int rangeOfAttack = 0;
	protected int alarmRange = 0;
	
	// wait timer 
	protected long waitTimer = System.nanoTime();
	protected long waitTime = (long)(0.5f * Main.NANO_SECOND);
	
	public BasicEnemy(Vector2f spawnPos, Vector2f size, Entity_ID entity_id, int fireRate) {
		super(spawnPos, size, entity_id, fireRate);
		
		ray = new Vector2f(spawnPos.x, spawnPos.y);
		
		int dir = Main.random.nextInt(40) / 10;
		switch(dir){
		case 0: // right
			direction.x = Main.RIGHT_DIR.x;
			direction.y = Main.RIGHT_DIR.y;
			break;
		case 1: // left
			direction.x = Main.LEFT_DIR.x;
			direction.y = Main.LEFT_DIR.y;
			break;
		case 2: // up
			direction.x = Main.UP_DIR.x;
			direction.y = Main.UP_DIR.y;
			break;
		case 3: // down
			direction.x = Main.DOWN_DIR.x;
			direction.y = Main.DOWN_DIR.y;
			break;
		}
	}

	public void update() {
		// check if visible(if it is do AI stuff)
		
		if(!dead){
			if(activated){
				AI();
			}else if(position.x + size.x/2 >= Main.renderer.camera.x - Main.WIDTH/2 && position.x - size.x/2 <= Main.renderer.camera.x + Main.WIDTH/2){
				if(position.y + size.y/2 >= Main.renderer.camera.y - Main.HEIGHT/2 && position.y - size.y/2 <= Main.renderer.camera.y + Main.HEIGHT/2){
					activated = true;
				}
			}
		}
		
		updateEnemy();
	}

	public abstract void updateEnemy();
	
	public void render() {
		renderEnemy();
	}

	public abstract void renderEnemy();
	
	private void AI(){
		// the player
		Entity player = Game.entityManager.getEntity(Entity_ID.player);
		
		// check if dead
		if(health <= 0){
			dead = true;
			return;
		}		
		
		// collision detection
		collisionX(direction);
		collisionY(direction);
		
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
					direction.x = Main.RIGHT_DIR.x;
					direction.y = Main.RIGHT_DIR.y;
					break;
				case 1: // left
					direction.x = Main.LEFT_DIR.x;
					direction.y = Main.LEFT_DIR.y;
					break;
				case 2: // up
					direction.x = Main.UP_DIR.x;
					direction.y = Main.UP_DIR.y;
					break;
				case 3: // down
					direction.x = Main.DOWN_DIR.x;
					direction.y = Main.DOWN_DIR.y;
					break;
				}
				
				direction.normalize();
			}
			
			
			if(!playerSpotted){
				if(raycastThePlayer(player.position.x, player.position.y)){
					state = ALERT;
					playerSpotted = true;
					activated = true;
					
					// alerting other enemies
					for(int i=0; i < Game.entityManager.getSize(); i++){
						// check if basic enemy
						if(Game.entityManager.getEntity(i).getEntity_ID() == Entity_ID.basic_enemy){
							// check if near
							BasicEnemy enemy = (BasicEnemy) Game.entityManager.getEntity(i);
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
			direction.x = player.position.x - position.x;
			direction.y = player.position.y - position.y;
			
			if(raycastThePlayer(player.position.x, player.position.y)){
				if(direction.length() <= minRangeOfAttack){
					state = AGGRESSIVE;
				}else{
					move(direction);
				}
			}else{
				state = MOVING_THE_PATH;
				playerSpotted = false;
			}
			
			break;
			
		case AGGRESSIVE: // attack the player
			direction.x = player.position.x - position.x;
			direction.y = player.position.y - position.y;
			attack();
			
			if(raycastThePlayer(player.position.x, player.position.y)){
				if(direction.length() >= minRangeOfAttack){
					state = ALERT;
				}else if(direction.length() >= rangeOfAttack){
					move(direction);
					System.out.println();
				}
			}else{
				state = MOVING_THE_PATH;
				playerSpotted = false;
			}
			
			break;
			
		case MOVING_THE_PATH:
			// create path movement
			state = IDLE;
			break;
		}
	}
	
	private int[] findPath(){
		int[] path = new int[1000];
		// TODO: A* algorithm; each integer in path array has a pointer to the next checkpoint
		return path;
	}
	
	private boolean raycastThePlayer(float playerX, float playerY){
		boolean seen = false;		
		
		Vector2f rayDir = new Vector2f(0,0);
		rayDir.x = playerX - position.x;
		rayDir.y = playerY - position.y;
		
		if(rayDir.length() < rangeOfSight){
			rayDir.normalize();
			seen = true;
			
			ray.x = position.x;
			ray.y = position.y;
			
			while((int)ray.x != (int)playerX && (int)ray.y != (int)playerY){
				ray.x += rayDir.x;
				ray.y += rayDir.y;
				
				if(Game.tileMapManager.isInTileSolid((int)ray.x, (int)ray.y)){
					System.out.println("FUCK OFFFFFF!!!!");
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
