package com.kingcoder.dungeon_crawler.game_managers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import com.kingcoder.dungeon_crawler.Main;
import com.kingcoder.dungeon_crawler.graphics.Sprite;
import com.kingcoder.dungeon_crawler.graphics.Texture;
import com.kingcoder.dungeon_crawler.handlers.ErrorLogger;

public class TileMapManager {

    public TileMap tileMap;

    public void render(){
        tileMap.render();
    }

    public void update(){
        tileMap.update();
    }

    // this function returns true if the coordinates locate in a solid tile
    public boolean isInTileSolid(int x, int y){
    	int tx = x / tileMap.getTileSize();
    	int ty = tileMap.getHeight() - (y / tileMap.getTileSize()) - 1;
    	try{
	        if(x > tileMap.getWidth() * tileMap.getTileSize() || x < 0 || y > tileMap.getHeight()  * tileMap.getTileSize() || y < 0){
	            return false;
	        }else if(tileMap.getTile(tx, ty) == TileMap.SOLID){
	            return true;
	        }
    	}catch(ArrayIndexOutOfBoundsException e){
    		System.out.println("X: " + x + "\t" + "Y: " + y);
    		System.out.println("Requested tile x: " + tx + "\t" + "Requested tile y: " + ty);
    		e.printStackTrace();
    	}
        
        return false;
    }

    public void loadMap(String path){
        int width;
        int height;
        int[][] tiles;
        int tileSize = 0;
        String[] tileTypes;
        String[] tileTextures;
        Sprite sprite;
        Texture texture;

        tileMap = new TileMap();

        try {
            // loading the tileSet info:
            InputStreamReader isr = new InputStreamReader(getClass().getResourceAsStream("/levels/" + path + "/" + path + ".tileset"));
            BufferedReader brInfo = new BufferedReader(isr);

            int numLines = 0;
            Vector<String> lines = new Vector<>();
            while (true){
                String line = brInfo.readLine();
                if(line == null) break;
                numLines++;
                lines.add(line);
            }


            tileTypes = new String[numLines-1];
            tileTextures = new String[numLines-1];
            for(int i=0; i < numLines; i++){
                String[] line = lines.get(i).split(",");

                if(i==0){
                    tileSize = Integer.parseInt(line[1]);
                    continue;
                }

                tileTextures[i-1] = line[0];
                tileTypes[i-1] = line[1];
            }
            brInfo.close();
            isr.close();

            // loading width, height and tiles keys:
            InputStreamReader isrTiles = new InputStreamReader(getClass().getResourceAsStream("/levels/" + path + "/" + path + ".map"));
            BufferedReader br = new BufferedReader(isrTiles);
            String s_Width = br.readLine();
            String s_Height = br.readLine();
            width = Integer.parseInt(s_Width);
            height = Integer.parseInt(s_Height);

            br.readLine();

            tiles = new int[height][width];
            for(int y = 0; y < height; y++){
                String[] line = br.readLine().split(",");
                for(int x = 0; x < width; x++){
                    tiles[y][x] = Integer.parseInt(line[x]);
                }
            }
            br.close();
            isrTiles.close();

            // creating the tiles:
            tileMap.tiles = new int[height][width];
            for(int i=0; i < tiles.length; i++){
                for(int j=0; j < tiles[i].length; j++){
                    int index = tiles[i][j]-1;
                    // checking if void tile
                    if(index < 0) continue;
                    String tileType = tileTypes[index];
                    tileMap.tiles[j][i] = Integer.parseInt(tileType);
                }
            }

            // creating the tile sprite
            int[] pixels = new int[tileSize*width * tileSize*height];
            for(int y=0; y < tileSize*height; y++){
                for(int x=0; x < tileSize*width; x++){
                    int indexX = x / tileSize;
                    int indexY = y / tileSize;
                    int pixelX = x - indexX * tileSize;
                    int pixelY = y - indexY * tileSize;

                    int textureIndex = tiles[indexY][indexX] - 1;
                    // checking if void tile
                    if(textureIndex < 0){
                        pixels[x + y * width*tileSize] = Main.renderer.TRANSPARENT_COLOUR;
                        continue;
                    }

                    String textureKey = tileTextures[textureIndex];
                    pixels[x + y * width*tileSize] = Main.textureManager.getTexture(textureKey).getPixel(pixelX, pixelY);
                }
            }

            texture = new Texture(pixels, tileSize*width, tileSize*height);
            sprite = new Sprite(texture, tileSize*width, tileSize*height);
            sprite.setPosition(0, 0);
            tileMap.setTileMapSprite(sprite);

            // setting attributes:
            tileMap.setPosition(0, 0);
            tileMap.setTileSize(tileSize);
            tileMap.setWidth(width);
            tileMap.setHeight(height);
        }catch (IOException e){
        	ErrorLogger.log(e);
        }
    }

    public void dispose(){
        tileMap.dispose();
    }


    public class TileMap{
        public int[][] tiles;
        private int tileSize;
        private int width;
        private int height;
        private Sprite tileMapSprite;

        public static final int VOID = 0;
        public static final int SOLID = 2;

        public void render(){
            tileMapSprite.render();
        }

        public void update(){
            tileMapSprite.update();
        }


        public int getTile(int x, int y){
            return tiles[x][y];
        }

        public int getTileSize(){
            return tileSize;
        }

        public int getHeight(){
            return height;
        }

        public int getWidth(){
            return width;
        }


        public void setPosition(float x, float y){
            tileMapSprite.setPosition(x, y);
        }

        public void setTileSize(int tileSize){
            this.tileSize = tileSize;
        }

        public void setWidth(int width){
            this.width = width;
        }

        public void setHeight(int height){
            this.height = height;
        }

        public void setTileMapSprite(Sprite tileMapSprite){
            this.tileMapSprite = tileMapSprite;
        }

        public void dispose(){
        }

    }

}
