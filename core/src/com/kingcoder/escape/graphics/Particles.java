package com.kingcoder.escape.graphics;

import java.util.Random;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kingcoder.escape.math.Vector2f;

public class Particles {

	private Random random;
	
	private boolean removed;
	private int numParticles;
	private boolean[] destroyed;
	private Vector2f[] positions;
	private Vector2f[] directions;
	private float speed;
	
	private int[] distances;
	
	private Texture texture;
	
	public Particles(int numParticles, int distance, float speed, int size, float red, float green, float blue, float alpha){
		removed = false;
		random = new Random();
		
		this.numParticles = numParticles;
		this.speed = speed;
		
		distances = new int[numParticles];
		for(int i=0; i < numParticles; i++){
			distances[i] = distance + random.nextInt(distance);
		}
		
		Pixmap pixmap = new Pixmap(size, size, Format.RGBA8888);
		for(int i=0; i < size; i++){
			for(int j=0; j < size; j++){
				pixmap.setColor(red, green, blue, alpha);
				pixmap.drawPixel(j, i);
			}
		}
		
		texture = new Texture(pixmap);
		pixmap.dispose();
		
		directions = new Vector2f[numParticles];
		positions = new Vector2f[numParticles];
		
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
			
			if(distances[i] <= 0){
				destroyed[i] = true;
				numDestroyed++;
			}else{
				positions[i].x += directions[i].x * speed;
				positions[i].y += directions[i].y * speed;	
				distances[i] -= speed;
			}
			
		}
		
		if(numDestroyed == numParticles){
			removed = true;
		}
	}
	
	public void render(SpriteBatch batch){
		for(int i=0; i < numParticles; i++){
			if(!destroyed[i]){
				batch.draw(texture, positions[i].x, positions[i].y);
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
		}
	}
	
	public void dispose(){
		texture.dispose();
	}
}
