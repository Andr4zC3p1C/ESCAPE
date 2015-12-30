package com.kingcoder.escape.scenes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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
    public abstract void renderDynamic(SpriteBatch dynamicBatch);
    public abstract void renderStatic(SpriteBatch staticBatch);
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
