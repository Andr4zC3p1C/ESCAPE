package com.kingcoder.dungeon_crawler.game_managers;

import java.util.LinkedList;

import com.kingcoder.dungeon_crawler.entities.Entity;
import com.kingcoder.dungeon_crawler.entities.Entity_ID;

public class EntityManager {

    private LinkedList<Entity> entities;

    public EntityManager(){
        entities = new LinkedList<>();
    }

    public void loadEntities(String path){
        // TODO: make entity loading from .properties file
    }

    public void addEntity(Entity entity){
        // adding the entity
        entities.addLast(entity);
        int index = entities.indexOf( entities.getLast());
        entities.getLast().setIndex(index);
    }

    public void update(){
        for(int i=0; i < entities.size(); i++){
            Entity entity = entities.get(i);
            if(entity.isRemoved()){
                entities.remove(i);
            }else{
                entities.get(i).update();
                entities.get(i).setIndex(i);
            }
        }
    }

    public void render(){
        for(int i=0; i < entities.size(); i++){
            entities.get(i).render();
        }
    }

    public Entity getEntity(int index){
        return entities.get(index);
    }
    
    public Entity getEntity(Entity_ID id){
        for(int i=0; i < entities.size(); i++){
        	Entity_ID entity_id = entities.get(i).getEntity_ID();
        	if(entity_id == id){
        		return entities.get(i);
        	}
        }
        
        return null;
    }
    
    public int getSize(){
    	return entities.size();
    }

}
