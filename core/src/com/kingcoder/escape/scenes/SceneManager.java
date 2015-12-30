package com.kingcoder.escape.scenes;

public class SceneManager {

    private Scene currentScene;

    public void setCurrentScene(Scene scene){
        if(currentScene != null){
            currentScene.dispose();
        }
        currentScene = scene;
        currentScene.init();
    }

    public Scene getCurrentScene(){
        return currentScene;
    }

}
