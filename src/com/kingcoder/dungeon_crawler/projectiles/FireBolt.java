package com.kingcoder.dungeon_crawler.projectiles;

import com.kingcoder.dungeon_crawler.Main;
import com.kingcoder.dungeon_crawler.graphics.Particles;
import com.kingcoder.dungeon_crawler.graphics.Sprite;
import com.kingcoder.dungeon_crawler.graphics.Texture;
import com.kingcoder.dungeon_crawler.math.Vector2f;

public class FireBolt extends Projectile{

	public FireBolt(Vector2f position, Vector2f direction, int ownerIndex) {
		super(position, direction, ownerIndex);
		speed = 3;
		damage = 50;
		range = 500;
		
		Texture tex = Main.textureManager.getTexture("fire_bolt");
		sprite = new Sprite(tex, tex.getWidth(), tex.getHeight());
		
		particles = new Particles(6, 0xFF3700, 10, 3);		
		particles.setSpawnPos(new Vector2f(position.x, position.y));
	}
}
