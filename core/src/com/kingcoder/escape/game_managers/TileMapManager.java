package com.kingcoder.escape.game_managers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kingcoder.escape.Main;
import com.kingcoder.escape.util.ErrorLogger;

public class TileMapManager {
	
	// TODO: add layers and dynamic world!
	
    public static final int VOID = 0;
    public static final int SOLID = 2;
    
    private int[][] tileTypes; // the types of tiles (SOLID(2), VOID(0), STATE(other))
    private String[][] tileSprites; // the sprites for tiles
    private int tileSize;
    private int width;
    private int height;
    
    // rendering the map
    private Texture mapTexture;
    private Pixmap pixmap;
    
    public void render(SpriteBatch batch){ 	
		batch.draw(mapTexture, 0.0f, 0.0f);
    }

    public void update(){
    	// dynamic world
    }
    
    // this function returns true if the coordinates locate in a solid tile
    public boolean isInTileSolid(int x, int y){
    	int tx = x / tileSize;
    	int ty = height - (y / tileSize) - 1;
    	try{
	        if(x > width * tileSize || x < 0 || y > height  * tileSize || y < 0){
	            return false;
	        }else if(tileTypes[ty][tx] == SOLID){
	            return true;
	        }
    	}catch(ArrayIndexOutOfBoundsException e){
    		System.out.println("X: " + x + "\t" + "Y: " + y);
    		System.out.println("Requested tile x: " + tx + "\t" + "Requested tile y: " + ty);
    		e.printStackTrace();
    	}
        
        return false;
    }

    public boolean isInTileSolid_C(int x, int y){
    	try{
	        if(x > width * tileSize || x < 0 || y > height  * tileSize || y < 0){
	            return false;
	        }else if(tileTypes[y][x] == SOLID){
	            return true;
	        }
    	}catch(ArrayIndexOutOfBoundsException e){
    		System.out.println("Requested tile x: " + x + "\t" + "Requested tile y: " + y);
    		e.printStackTrace();
    	}
        
        return false;
    }
    
    public void loadMap(String path){
        int[][] tiles;
        String[] tileTypes;
        String[] tileTextures;           

        try {
            // loading width, height and tiles keys:
            FileHandle file = Gdx.files.internal("levels/" + path + "/" + path + ".map");
            BufferedReader br = file.reader((int)file.length());

            tileSize = Integer.parseInt(br.readLine().split(" = ")[1]);
            br.readLine();
            
            int numLines = 0;
            ArrayList<String> lines = new ArrayList<String>();
            while (true){
                String line = br.readLine();
                if(line.startsWith("-")) break;
                numLines++;
                lines.add(line);
            }

            tileTypes = new String[numLines];
            tileTextures = new String[numLines];
            for(int i=0; i < numLines; i++){
                String[] line = lines.get(i).split(" = ");

                tileTextures[i] = line[0];
                tileTypes[i] = line[1];
            }
            
            width = Integer.parseInt(br.readLine());
            height = Integer.parseInt(br.readLine());
            
            br.readLine();

            tiles = new int[height][width];
            for(int y = 0; y < height; y++){
                String[] line = br.readLine().split(",");
                for(int x = 0; x < width; x++){
                    tiles[y][x] = Integer.parseInt(line[x]);
                }
            }
            br.close();

            // creating the tile types
            this.tileTypes = new int[height][width];
            for(int y=0; y < height; y++){
                for(int x=0; x < width; x++){
                    int index = tiles[y][x]-1;
                    // checking if void tile
                    if(index < 0){
                    	this.tileTypes[y][x] = 1;
                    	continue;
                    }
                    
                    this.tileTypes[y][x] = Integer.parseInt(tileTypes[index]);
                }
            }

            // creating the tile texture
            pixmap = Main.textureManager.loadTileSet(tiles, tileSize);
        }catch (IOException e){
        	ErrorLogger.log(e);
        }
    }

    public void initTexture(){
    	mapTexture = new Texture(pixmap);
    	mapTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    	pixmap.dispose();
    }
    

    public int getWidth(){
    	return width;
    }
    
    public int getHeight(){
    	return height;
    }
    
    public int getTileSize(){
    	return tileSize;
    }
    
    public void dispose(){
    	mapTexture.dispose();
    	pixmap.dispose();
    }
}
