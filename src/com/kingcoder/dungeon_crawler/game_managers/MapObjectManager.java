package com.kingcoder.dungeon_crawler.game_managers;

import java.util.LinkedList;

import com.kingcoder.dungeon_crawler.entities.Entity;

public class MapObjectManager {

    private LinkedList<Entity> mapObjects; // TODO: create map objects

    public MapObjectManager(){
        mapObjects = new LinkedList<>();
    }

    public void update(){
        for(int i=0; i < mapObjects.size(); i++){
            mapObjects.get(i).update();
        }
    }

    public void render(){
        for(int i=0; i < mapObjects.size(); i++){
            mapObjects.get(i);
        }
    }

}
