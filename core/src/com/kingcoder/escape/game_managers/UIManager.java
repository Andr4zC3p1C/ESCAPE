package com.kingcoder.escape.game_managers;

import java.awt.Graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.kingcoder.escape.scenes.Game;

public class UIManager {

	// TODO: create buttons & texts
    public void render(Graphics g){

    }

    public void update(){
    	// if ESCAPE pressed then PAUSE or UNPAUSE the game
    	if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)){
    		Game.nPaused();
    	}
    }

}
