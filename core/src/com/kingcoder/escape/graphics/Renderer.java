package com.kingcoder.escape.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
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
		dynamicBatch = new SpriteBatch(1000, createDefaultShader());
		staticBatch = new SpriteBatch(1000, createDefaultShader());
		
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
	
	
	// SHADER FIX:
	public static ShaderProgram createDefaultShader() {
		   String vertexShader = "#version 330 core\n"
		      + "in vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
		      + "in vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
		      + "in vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
		      + "uniform mat4 u_projTrans;\n" //
		      + "out vec4 v_color;\n" //
		      + "out vec2 v_texCoords;\n" //
		      + "\n" //
		      + "void main()\n" //
		      + "{\n" //
		      + "   v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
		      + "   v_color.a = v_color.a * (255.0/254.0);\n" //
		      + "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
		      + "   gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
		      + "}\n";
		   String fragmentShader = "#version 330 core\n"
		      + "#ifdef GL_ES\n" //
		      + "#define LOWP lowp\n" //
		      + "precision mediump float;\n" //
		      + "#else\n" //
		      + "#define LOWP \n" //
		      + "#endif\n" //
		      + "in LOWP vec4 v_color;\n" //
		      + "in vec2 v_texCoords;\n" //
		      + "out vec4 fragColor;\n" //
		      + "uniform sampler2D u_texture;\n" //
		      + "void main()\n"//
		      + "{\n" //
		      + "  fragColor = v_color * texture(u_texture, v_texCoords);\n" //
		      + "}";

		   ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);
		   if (shader.isCompiled() == false) throw new IllegalArgumentException("Error compiling shader: " + shader.getLog());
		   return shader;
	}
	
	public void dispose(){
		dynamicBatch.dispose();
		staticBatch.dispose();
	}
	
}
