package com.kingcoder.dungeon_crawler.graphics;

import com.kingcoder.dungeon_crawler.Main;
import com.kingcoder.dungeon_crawler.math.Vector2f;

public class Sprite {
    private int width, height;
    private Vector2f pos;
    private Vector2f rendPos;
    private Texture texture;

    /*
    *
    * TODO: add sprite ROTATION & SCALE
    *
    * */

    public Sprite(Texture texture, int width, int height){
        this.width = width;
        this.height = height;
        this.texture = texture;
        rendPos = new Vector2f(0,0);
    }

    public void render() {
        texture.render(rendPos);
    }


    public void render(int x, int y){
        rendPos.x = x - Main.renderer.camera.x + Main.WIDTH/2;
        rendPos.y = Main.HEIGHT - (y - Main.renderer.camera.y + Main.HEIGHT/2)  - height;
        texture.render(rendPos);
    }

    public void update(){
        rendPos.x = pos.x - Main.renderer.camera.x + Main.WIDTH/2;
        rendPos.y = Main.HEIGHT - (pos.y - Main.renderer.camera.y + Main.HEIGHT/2) - height;
    }

    public void setPosition(float x, float y){
        pos = new Vector2f(x, y);
    }

    public void setPosition(Vector2f position){
        pos = position;
    }

    public void setPositionCenter(float x, float y){
        pos = new Vector2f(x - width/2, y - height/2);
    }

    public void setPositionCenter(Vector2f position){
        pos = new Vector2f(position.x - width/2, position.y - height/2);
    }

    public void setWidth(int width){
        this.width = width;
    }

    public void setHeight(int height){
        this.height = height;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
    
    public int getWidth(){ return width; }
    
    public int getHeight(){ return height; }

}
