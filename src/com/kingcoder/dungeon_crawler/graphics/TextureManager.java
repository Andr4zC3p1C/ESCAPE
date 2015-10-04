package com.kingcoder.dungeon_crawler.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

import javax.imageio.ImageIO;

import com.kingcoder.dungeon_crawler.handlers.ErrorLogger;

public class TextureManager {

    private HashMap<String, Texture> textures;
    private int[] pixels;

    public TextureManager(){
        textures = new HashMap<>();
    }

    public void loadSpriteSheet(String path){
        try {
            BufferedImage sheetImage = ImageIO.read(getClass().getResourceAsStream("/textures/" + path + "/" + path + ".png"));
            int w = sheetImage.getWidth();
            int h = sheetImage.getHeight();
            pixels = new int[w * h];
            sheetImage.getRGB(0, 0, w, h, pixels, 0, w);

            InputStream in = getClass().getResourceAsStream("/textures/" + path + "/" + path + ".tex");
            Properties p = new Properties();
            p.load(in);
            Set<String> k = p.stringPropertyNames();
            String[] keys = new String[k.size()];
            k.toArray(keys);
            for(int i = 0; i < keys.length; i++){
                String val = p.getProperty(keys[i]);
                String[] values = val.split(",");
                int x = Integer.parseInt(values[0]);
                int y = Integer.parseInt(values[1]);
                int width = Integer.parseInt(values[2]);
                int height = Integer.parseInt(values[3]);

                int[] spritePixels = new int[width * height];
                for(int a=0; a < width; a++){
                    for(int b=0; b < height; b++){
                        spritePixels[a + b * width] = pixels[(a+x) + (b+y) * w];
                    }
                }
                textures.put(keys[i], new Texture(spritePixels, width, height));
            }
            in.close();
        }catch(IOException e){
        	ErrorLogger.log(e);
        }


    }

    public void removeTexture(String key){
        textures.remove(key);
    }
    public Texture getTexture(String key) { return textures.get(key);}
    public void clear(){
        textures.clear();
    }

}
