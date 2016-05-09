package com.kingcoder.escape.entities;

/**
 * A tilemap class, used to represent a 2D-tilemap.
 */
public class TileMap {

    private int[][][] tileTypes;                    // Types of tiles in the map [0-VOID, 1-2-SOLID, 3-DESTRUCTIBLE]
    private int width, height;                      // The width and the height of the map in tiles
    private int tileSize;                           // The size of a tile in pixels

    private String[] textureKeys;                   // The keys of the sprites of the map
    private int[][][] textures;                     // The indexes of keys for tiles

    /**
     * Creates a TileMap object and initializes the arrays
     * @param width of the map in tiles
     * @param height of the map in tiles
     * @param numLayers of layers in the tile
     */
    public TileMap(int width, int height, int numLayers, int tileSize){
        this.tileSize = tileSize;
        this.width = width;
        this.height = height;
        tileTypes = new int[numLayers][width][height];
        textures = new int[numLayers][width][height];
    }

    /**
     * Loading the map into the arrays
     */
    public void load(int[][][] textures){

    }

    public void render(){
        // the layers
        for(int i=0; i < tileTypes.length; i++){

        }
    }
}
