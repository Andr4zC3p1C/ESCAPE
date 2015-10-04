package com.kingcoder.dungeon_crawler.scenes;

import java.awt.Graphics;

public abstract class Scene {

    protected boolean initialized = false;
    protected SceneID sceneID;

    // scene ID-s
    public enum SceneID{
        TextureLoader,
        MainMenu,
        Game
    }

    public abstract void init();
    public abstract void update();
    public abstract void render();
    public abstract void renderHighRes(Graphics g);
    public abstract void dispose();

    public boolean isInitialized(){
        return initialized;
    }

    public void setSceneID(SceneID id){
        sceneID = id;
    }

    public SceneID getSceneID(){
        return sceneID;
    }

}
