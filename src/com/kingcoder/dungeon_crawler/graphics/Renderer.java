package com.kingcoder.dungeon_crawler.graphics;

import com.kingcoder.dungeon_crawler.math.Vector2f;

public class Renderer {

	public static final int TRANSPARENT_COLOUR = -65281;
	
    private int width, height;
    public int[] pixels;
    private int clearColor = 0x000000;  // pure black

    public Vector2f camera;
    public Vector2f prevCamera;
    
    public Renderer(int width, int height){
        this.width = width;
        this.height = height;
        camera = new Vector2f(0, 0);
        prevCamera = camera;
        pixels = new int[width * height];
    }

    public void clear(){
        for(int i=0; i < pixels.length; i++){
            pixels[i] = clearColor;
        }
    }

    public void render(int[] pixels, int xPos, int yPos, int width, int height){
        for(int y=0; y < height; y++){
            for(int x=0; x < width; x++){

                int INDEX = (x+xPos) + (y+yPos) * this.width;
                if((x+xPos) < 0 || (x+xPos) >= this.width)continue;
                if(INDEX >= this.width * this.height || INDEX < 0) break;

                // transparency if pink color(0xFF00FF) ----->>> there are is also alpha in RGB value so just 0xff00ff won't work (it's actually 0xFFFFFFFFFFFF00FF)
                if(pixels[x + y * width] == TRANSPARENT_COLOUR) continue;

                this.pixels[INDEX] = pixels[x + y * width];
            }
        }
    }

    public void drawRect(int x, int y, int width, int height, int colour){
        int[] pixels = new int[width * height];
        for(int i=0; i < pixels.length; i++){
            pixels[i] = colour;
        }
        render(pixels, x, y, width, height);
    }

}
