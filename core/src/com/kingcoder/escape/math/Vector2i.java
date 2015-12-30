package com.kingcoder.escape.math;

public class Vector2i {

    public int x, y;

    public Vector2i(int x, int y){
        this.x = x;
        this.y = y;
    }

    public float length(){
        return (float)Math.sqrt(x*x + y*y);
    }

    public Vector2i add(Vector2i v){
    	this.x += v.x;
    	this.y += v.y;
    	return this;
    }
    
    public static Vector2i add(Vector2i v1, Vector2i v2){
    	return new Vector2i(v1.x + v2.x, v1.y + v2.y);
    }
    
    public Vector2i subtract(Vector2i v){
    	this.x -= v.x;
    	this.y -= v.y;
    	return this;
    }
    
    public static Vector2i subtract(Vector2i v1, Vector2i v2){
    	return new Vector2i(v1.x - v2.x, v1.y - v2.y);
    }
    
}
