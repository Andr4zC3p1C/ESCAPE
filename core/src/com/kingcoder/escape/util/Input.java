package com.kingcoder.escape.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.kingcoder.escape.Main;
import com.kingcoder.escape.math.Vector2f;

public class Input implements KeyListener, MouseListener, MouseMotionListener{

    // KEYBOARD
    private boolean[] i_keys = new boolean[256];
    private boolean[] keys = new boolean[256];
    private boolean[] justPressed = new boolean[256];
    private boolean anyKeyPressed = false;

    public void keyTyped(KeyEvent e){

    }

    public void keyPressed(KeyEvent e){
        if(keys[e.getKeyCode()] && justPressed[e.getKeyCode()]){
            justPressed[e.getKeyCode()] = false;
        } else if (!keys[e.getKeyCode()]) {
            keys[e.getKeyCode()] = true;
            justPressed[e.getKeyCode()] = true;
        }
    }

    public void keyReleased(KeyEvent e){
        i_keys[e.getKeyCode()] = false;
    }

    public boolean keyPressed(int keyCode){
        return justPressed[keyCode];
    }

    public boolean anyKeyPressed(){
        return anyKeyPressed;
    }

    public boolean keyDown(int keyCode){
        return keys[keyCode];
    }


    // MOUSE
    private Vector2f mousePosition = new Vector2f(0,0);
    private boolean[] mouseButtonsPress = new boolean[256];
    private boolean[] mouseButtonsClick = new boolean[256]; 

    public void mouseClicked(MouseEvent e){
    	
    }

    public void mousePressed(MouseEvent e){
    	mouseButtonsPress[e.getButton()] = true;
    }

    public void mouseReleased(MouseEvent e){
    	mouseButtonsPress[e.getButton()] = false;
    }

    public void mouseEntered(MouseEvent e){}

    public void mouseExited(MouseEvent e){}

    public void mouseDragged(MouseEvent e){}

    public void mouseMoved(MouseEvent e){
    	mousePosition.x = e.getX();
    	mousePosition.y = Main.HEIGHT - e.getY();
    }

    
    public boolean mouseButtonPressed(int keycode){
    	return mouseButtonsPress[keycode];
    }
    
    public Vector2f getMousePosition(){
    	return mousePosition;
    }
    
    public int getMouseX(){
    	return (int)mousePosition.x;
    }
    
    public int getMouseY(){
    	return (int)mousePosition.y;
    }
    

    public void update(){
        // keyboard
        keys = i_keys;
        anyKeyPressed = false;
        for(int i = 0; i < keys.length; i++){
            if(keys[i]){
                anyKeyPressed = true;
                break;
            }
        }
       
    }

}
