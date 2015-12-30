package com.kingcoder.escape.graphics;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kingcoder.escape.math.Vector2i;

public class TextureManager {

	public static final int TEXTURE_REGION = 1;
	public static final int SPRITE = 2;
	
    private HashMap<String, Texture> textures;
    private HashMap<String, TextureRegion> textureRegions;
    private HashMap<String, Sprite> sprites;

    public TextureManager(){
    	textures = new HashMap<String, Texture>();
    	textureRegions = new HashMap<String, TextureRegion>();
    	sprites = new HashMap<String, Sprite>();
    }
    
    /**
     * The method loads the pixmap into GPU RAM and creates a Sprite / TextureRegion if needed
     * 
     * @param pixmap
     * @param path
     */
    public void createTexture(Pixmap pixmap, String path){ 	
    	Texture texture = new Texture(pixmap);
    	texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    	textures.put(path, texture);
    	
    	String texData = Gdx.files.internal("textures/" + path + "/" + path + ".tex").readString();
    	String[] lines = texData.split("\n");
    	int type = Integer.parseInt(lines[0].split(" = ")[1].trim());
    	
    	if(type == TEXTURE_REGION || type == SPRITE){
            String[] keys = new String[lines.length-1];
            for(int i = 1; i < lines.length; i++){
            	String[] line = lines[i].split(" = ");
            	keys[i-1] = line[0];
                String val = line[1];
                val = val.trim();
                String[] values = val.split(",");
                int x = Integer.parseInt(values[0]);
                int y = Integer.parseInt(values[1]);
                int width = Integer.parseInt(values[2]);
                int height = Integer.parseInt(values[3]);

                TextureRegion region = new TextureRegion(texture, x, y, width, height);
                fixBleeding(region);
                
                // adding to the HashMap
        		if(type == SPRITE){
        			sprites.put(keys[i-1], new Sprite(region));
        		}else{
        			textureRegions.put(keys[i-1], region);
        		}
            }
    	}	
    }
    
    /**
     * The method creates a Pixmap of the whole tile_map and returns it
     * 
     * @param tileMap
     * @param tileSize
     * @return a Pixmap of the tile_map
     */
    public Pixmap loadTileSet(int[][] tileMap, int tileSize){
    	// TODO: add multiple layers support
    	
    	Pixmap pixmap = new Pixmap(tileMap[0].length * tileSize, tileMap.length * tileSize, Format.RGBA8888);
    	
    	// reading the tileset
    	Pixmap tileSet = new Pixmap(Gdx.files.internal("textures/tileset/tileset.png"));
    	
    	Vector2i[] tiles = new Vector2i[(tileSet.getWidth() / tileSize) * (tileSet.getHeight() / tileSize)];
    	String[] textureLines = Gdx.files.internal("textures/tileset/tileset.tex").readString().split("\n");
    	for(int i = 1; i < textureLines.length; i++){
        	String[] line = textureLines[i].split(" = ");
            String val = line[1];
            val = val.trim();
            String[] values = val.split(",");
            tiles[i-1] = new Vector2i(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
        }
    	
    	for(int y=0; y < tileMap.length; y++){
    		for(int x=0; x < tileMap[0].length; x++){
    			if(tileMap[y][x] == 0){
    				// drawing to pixmap
    				for(int yP = 0; yP < tileSize; yP++){
    					for(int xP = 0; xP < tileSize; xP++){
        					pixmap.setColor(0, 0, 0, 0);
        					pixmap.drawPixel(xP + tileSize * x, yP + tileSize * y);
        				}
    				}
    			}else{
    				// drawing to pixmap
    				for(int yP = 0; yP < tileSize; yP++){
    					for(int xP = 0; xP < tileSize; xP++){
    						int color = tileSet.getPixel(tiles[tileMap[y][x]-1].x + xP, tiles[tileMap[y][x]-1].y + yP);
        					pixmap.setColor(color);
        					pixmap.drawPixel(xP + tileSize * x, yP + tileSize * y);
        				}
    				}
    			}
    		}
    	}
    	
    	tileSet.dispose();
    	
    	return pixmap;
    }
    
    /**
     * 
     * The method changes the given texture at the x and y coordinates of the texture.
     * The pixels are overwritten with the given pixmap.
     * 
     * @param changePixmap
     * @param x
     * @param y
     * @param textureKey
     */
    public void changeTexture(Pixmap changePixmap, int x, int y, String textureKey){
    	Texture texture = textures.get(textureKey);
    	Pixmap pixmap = texture.getTextureData().consumePixmap();
    	
    	// checking if out of bounds
    	if((x < 0 || x + changePixmap.getWidth() > pixmap.getWidth()) || (y < 0 || y + changePixmap.getHeight() > pixmap.getHeight())){
    		changePixmap.dispose();
        	pixmap.dispose();
    		return;
    	}
    	
    	for(int yi = 0; yi < changePixmap.getHeight(); yi++){
    		for(int xi = 0; xi < changePixmap.getWidth(); xi++){
    			pixmap.setColor(changePixmap.getPixel(xi, yi));
    			pixmap.drawPixel(xi + x, yi + y);
    		}
    	}
    	
    	removeTexture(textureKey);
    	textures.put(textureKey, new Texture(pixmap));
    	changePixmap.dispose();
    	pixmap.dispose();
    }

    public void removeTexture(String key){
    	textures.get(key).dispose();
    	textures.remove(key);
    }
    
    public Sprite getSprite(String key) { return sprites.get(key);}
    
    public Texture getTexture(String texture){
    	return textures.get(texture);
    }
    
    public TextureRegion getTextureRegion(String textureRegion){
    	return textureRegions.get(textureRegion);
    }
    
    
    /**
     * @return a string of containing elements
     */
    public String toString(){
    	StringBuilder sb = new StringBuilder();
    	
    	// Texture HashMap
    	String[] textureKeys = new String[textures.size()];
    	textures.keySet().toArray(textureKeys);
    	sb.append("Textures: [");
    	for(int i=0; i < textureKeys.length - 1; i++){
    		sb.append(textureKeys[i] + ", ");
    	}
    	sb.append(textureKeys[textureKeys.length - 1] + "]\n");
    	
    	// TextureRegion HashMap
    	String[] textureRKeys = new String[textureRegions.size()];
    	textureRegions.keySet().toArray(textureRKeys);
    	sb.append("Texture Regions: [");
    	for(int i=0; i < textureRKeys.length - 1; i++){
    		sb.append(textureRKeys[i] + ", ");
    	}
    	sb.append(textureRKeys[textureRKeys.length -1] + "]\n");
    	
    	// Sprite HashMap
    	String[] spriteKeys = new String[sprites.size()];
    	sprites.keySet().toArray(spriteKeys);
    	sb.append("Sprites: [");
    	for(int i=0; i < spriteKeys.length - 1; i++){
    		sb.append(spriteKeys[i] + ", ");
    	}
    	sb.append(spriteKeys[spriteKeys.length - 1] + "]\n");
    	
    	return sb.toString();
    }
    
    
    public static void fixBleeding(TextureRegion region) {
    	float x = region.getRegionX();
    	float y = region.getRegionY();
    	float width = region.getRegionWidth();
    	float height = region.getRegionHeight();
    	float invTexWidth = 1f / region.getTexture().getWidth();
    	float invTexHeight = 1f / region.getTexture().getHeight();
    	region.setRegion((x + .5f) * invTexWidth, (y+.5f) * invTexHeight, (x + width - .5f) * invTexWidth, (y + height - .5f) * invTexHeight);       
    }
    
    
    public void clear(){
    	String[] keys = new String[textures.size()];
    	textures.keySet().toArray(keys);
    	for(int i=0; i < keys.length; i++){
    		textures.get(keys[i]).dispose();
    	}
    	
    	textures.clear();
    	sprites.clear();
    }

}
