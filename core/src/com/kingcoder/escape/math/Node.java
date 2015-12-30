package com.kingcoder.escape.math;

public class Node {

	public Vector2i pos;
	public int g;
	public int h;
	public int f;
	public Vector2i parent;
	public int pathCounter;
	
	public Node(){
		pos = new Vector2i(0,0);
	}
	
	public void initH(int x, int y){
		h = Math.abs(x - this.pos.x) + Math.abs(y - this.pos.y);
	}
	
	public int distance(Vector2i pos){
		int _x = Math.abs(pos.x - this.pos.x);
		int _y = Math.abs(pos.x - this.pos.x);
		float distance = (float)Math.sqrt(_x*_x + _y*_y);
		return (int)(distance * 10);
	}
	
}
