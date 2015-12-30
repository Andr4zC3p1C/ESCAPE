package com.kingcoder.escape.projectiles;

import com.kingcoder.escape.graphics.Particles;
import com.kingcoder.escape.math.Vector2f;

public class FireBolt extends Projectile{

	public FireBolt(Vector2f position, Vector2f direction, int ownerIndex) {
		super(position, direction, ownerIndex, "fire_bolt");
		speed = 6;
		damage = 50;
		range = 1000;
		
		particles = new Particles(60, 40, 8, 1.0f, 0.0f, 0.0f, 1.0f);
		particles.setSpawnPos(new Vector2f(position.x, position.y));
	}
}
