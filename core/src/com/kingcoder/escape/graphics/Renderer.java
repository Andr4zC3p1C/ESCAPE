package com.kingcoder.escape.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kingcoder.escape.Main;
import com.kingcoder.escape.math.Vector2f;

public class Renderer {

	private SpriteBatch dynamicBatch; // batch for everything that moves (e.g. map, map objects, entities, projectiles, particles, ...)
	private SpriteBatch staticBatch; // batch for everything that remains stationary(e.g. HUD, UI)
	
	// the camera
	private OrthographicCamera camera;
	public Vector2f cameraPos;
	
	// fonts
	public BitmapFont font_title = new BitmapFont(Gdx.files.internal("fonts/Adler_title.fnt"));
	public BitmapFont font_normal = new BitmapFont(Gdx.files.internal("fonts/Adler_normal.fnt"));
	
	// cursors
	public Cursor default_Cursor;
	public Cursor aim_Cursor;
	public Cursor onTarget_Cursor;
	
	public Renderer(){
		camera = new OrthographicCamera(Main.WIDTH, Main.HEIGHT);
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		cameraPos = new Vector2f(Main.WIDTH / 2, Main.HEIGHT / 2);
		
		// the batches
		dynamicBatch = new SpriteBatch();
		staticBatch = new SpriteBatch();
		
		loadCursors();
		Gdx.graphics.setCursor(aim_Cursor);
	}
	
	public void render(){
		//System.out.println(camera.position.x + " , " + camera.position.y);
		dynamicBatch.setProjectionMatrix(camera.combined);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if(Main.sceneManager.getCurrentScene().isInitialized()) {
			// dynamic rendering
			dynamicBatch.begin();
            	Main.sceneManager.getCurrentScene().renderDynamic(dynamicBatch);
            dynamicBatch.end();
            
            // static rendering
            staticBatch.begin();
	            Main.sceneManager.getCurrentScene().renderStatic(staticBatch);
	            
	            // rendering hte fps
	            if(Main.PRINT_FPS_UPS)
	            	font_normal.draw(staticBatch, "FPS: " + Main.FPS, 20, Main.HEIGHT - 20);
            
            staticBatch.end();
        }
	}
	
	public void loadCursors(){
		// default cursor
		Pixmap defaultCPixmap = new Pixmap(Gdx.files.internal("cursors/default_cursor.png"));
		default_Cursor = Gdx.graphics.newCursor(defaultCPixmap, 0, 0);
		defaultCPixmap.dispose();
		
		// aim cursor
		Pixmap aimCPixmap = new Pixmap(Gdx.files.internal("cursors/aim_cursor.png"));
		aim_Cursor = Gdx.graphics.newCursor(aimCPixmap, aimCPixmap.getWidth() / 2, aimCPixmap.getHeight() / 2);
		aimCPixmap.dispose();
		
		// on_target cursor
		Pixmap onTargetCPixmap = new Pixmap(Gdx.files.internal("cursors/onTarget_cursor.png"));
		onTarget_Cursor = Gdx.graphics.newCursor(onTargetCPixmap, onTargetCPixmap.getWidth() / 2, onTargetCPixmap.getHeight() / 2);
		onTargetCPixmap.dispose();
	}
	
	public void translateCamera(Vector2f trans){
		camera.translate(trans.x, trans.y);
		cameraPos.add(trans);
		camera.update();
	}
	
	public void translateCamera(float x, float y){
		camera.translate(x, y);
		cameraPos.x += x;
		cameraPos.y += y;
		camera.update();
	}
	
	public void setCameraPosition(Vector2f vec){
		camera.position.x = vec.x;
		camera.position.y = vec.y;
		cameraPos.x = vec.x;
		cameraPos.y = vec.y;
		camera.update();
	}
	
	public void dispose(){
		dynamicBatch.dispose();
		staticBatch.dispose();
	}
	
}
