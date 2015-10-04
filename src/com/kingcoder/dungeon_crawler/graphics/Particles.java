package com.kingcoder.dungeon_crawler.graphics;

import java.util.ArrayList;
import java.util.Random;

import com.kingcoder.dungeon_crawler.Main;
import com.kingcoder.dungeon_crawler.math.Vector2f;

public class Particles {

	private Random random;
	
	private boolean removed;
	private int numParticles;
	private boolean[] destroyed;
	private ArrayList<Sprite> particles;
	private Vector2f[] positions;
	private Vector2f[] directions;
	private float speed;
	
	private int[] distances;
	private int[] distancesTraveled;
	
	public Particles(int numParticles, int color, int distance, float speed){
		removed = false;
		random = new Random();
		
		this.numParticles = numParticles;
		this.speed = speed;
		
		distances = new int[numParticles];
		distancesTraveled = new int[numParticles];
		for(int i=0; i < numParticles; i++){
			distances[i] = distance + random.nextInt(distance / 5);
			distancesTraveled[i] = 0;
		}
		
		// setting the texture
		int[] pixels = new int[2 * 2];
		for(int i=0; i < pixels.length; i++){
			pixels[i] = color;
		}
		
		// creating sprites, adding positions & setting directions
		particles = new ArrayList<>();
		directions = new Vector2f[numParticles];
		positions = new Vector2f[numParticles];
		for(int i=0; i < numParticles; i++){
			particles.add(new Sprite(new Texture(pixels, 2, 2), 2, 2));
		}
		
		// Initializing destroyed array
		destroyed = new boolean[numParticles];
		for(int i=0; i < numParticles; i++){
			destroyed[i] = false;
		}
	}
	
	public void update(){
		int numDestroyed = 0;
		for(int i=0; i < numParticles; i++){
			if(destroyed[i]){
				numDestroyed++;
				continue;
			}
			
			if(distancesTraveled[i] > distances[i]){
				destroyed[i] = true;
				numDestroyed++;
			}else{
				positions[i].x += directions[i].x * speed;
				positions[i].y += directions[i].y * speed;	
				distancesTraveled[i] += new Vector2f(directions[i].x * speed, directions[i].y * speed).length();
			}
			
			particles.get(i).setPositionCenter(positions[i]);
			particles.get(i).update();
		}
		
		if(numDestroyed == numParticles){
			removed = true;
		}
	}
	
	public void render(){
		for(int i=0; i < numParticles; i++){
			if(!destroyed[i]){
				particles.get(i).render();
			}
		}
	}
	
	public boolean isRemoved(){
		return removed;
	}
	
	
	public void setSpawnPos(Vector2f spawnPos){
		for(int i=0; i < numParticles; i++){		
			Vector2f direction = new Vector2f(random.nextInt(100), random.nextInt(100));
			direction = Vector2f.subtract(direction, new Vector2f(50, 50));
			direction.normalize();
			
			directions[i] = new Vector2f(direction.x, direction.y);
			positions[i] = new Vector2f(spawnPos.x, spawnPos.y);

			particles.get(i).setPositionCenter(positions[i]);
		}
	}
}
