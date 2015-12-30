package com.kingcoder.escape.game_managers;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kingcoder.escape.entities.Entity;

public class MapObjectManager {

    private LinkedList<Entity> mapObjects; // TODO: create map objects

    public MapObjectManager(){
        mapObjects = new LinkedList<Entity>();
    }

    public void update(){
        for(int i=0; i < mapObjects.size(); i++){
            mapObjects.get(i).update();
        }
    }

    public void render(SpriteBatch batch){
        for(int i=0; i < mapObjects.size(); i++){
            mapObjects.get(i);
        }
    }

}
