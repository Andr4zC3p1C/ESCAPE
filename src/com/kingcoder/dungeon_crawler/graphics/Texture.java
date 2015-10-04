package com.kingcoder.dungeon_crawler.graphics;

import com.kingcoder.dungeon_crawler.Main;
import com.kingcoder.dungeon_crawler.math.Vector2f;

public class Texture {

    public int[] pixels;
    private int width, height;

    public Texture(int[] pixels, int width, int height){
        this.width = width;
        this.height = height;
        this.pixels = pixels;
    }

    public void render(Vector2f rendPos){
        Main.renderer.render(pixels, (int)rendPos.x, (int)rendPos.y, width, height);
    }

    public int getPixel(int x, int y){
        return pixels[x + y * width];
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

}
