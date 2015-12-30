package com.kingcoder.escape.math;

public class Vector2f {

    public float x, y;

    public Vector2f(float x, float y){
        this.x = x;
        this.y = y;
    }

    public void normalize(){
        float length = length();
        x /= length;
        y /= length;
    }

    public float length(){
        return (float)Math.sqrt(x*x + y*y);
    }

    public Vector2f add(Vector2f v){
    	this.x += v.x;
    	this.y += v.y;
    	return this;
    }
    
    public static Vector2f add(Vector2f v1, Vector2f v2){
    	return new Vector2f(v1.x + v2.x, v1.y + v2.y);
    }
    
    public Vector2f subtract(Vector2f v){
    	this.x -= v.x;
    	this.y -= v.y;
    	return this;
    }
    
    public static Vector2f subtract(Vector2f v1, Vector2f v2){
    	return new Vector2f(v1.x - v2.x, v1.y - v2.y);
    }
    
}
