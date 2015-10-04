package com.kingcoder.dungeon_crawler;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.kingcoder.dungeon_crawler.handlers.ErrorLogger;

public class Window extends JFrame{

    public static final int DEFAULT_FLAG = 0;
    public static final int FULL_SCREEN = 1;
    public static final int WINDOWED = 2;

    private int width, height, flags;
    private boolean showFpsUps;
    
    public Window(){
    	super();
    	
    	readFromSettings();
    	
        // settings
        if((flags & FULL_SCREEN) == FULL_SCREEN){
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int s_width = (int)screenSize.getWidth();
            int s_height = (int)screenSize.getHeight();
            Main.WIDTH = (int)(s_width / Main.SCALE);
            Main.HEIGHT = (int)(s_height / Main.SCALE);
            setUndecorated(true);
        }else if((flags & WINDOWED) == WINDOWED){
            Main.WIDTH = (int)(width / Main.SCALE);
            Main.HEIGHT = (int)(height / Main.SCALE);
        }else if(flags == DEFAULT_FLAG){
        	Main.WIDTH = (int)(1024 / Main.SCALE);
            Main.HEIGHT = (int)(768 / Main.SCALE);
        }

        setResizable(false);
        
        // creating the window title
        String title = Main.TITLE + "     " + Main.VERSION + "     " + Main.WIDTH*Main.SCALE + " x " + Main.HEIGHT*Main.SCALE;
        setTitle(title);
        
        // adding the icon
        BufferedImage image = null;
        try {
			image = ImageIO.read(getClass().getResource("/icons/icon.png"));
			setIconImage(image);		
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        Main.PRINT_FPS_UPS = showFpsUps;
        
        Main main = new Main();
        add(main);
        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setVisible(true);

        // Running the game
        if(main.init() != 0){
        	ErrorLogger.log("Couldn't initialize main");
        }
    }

    private void readFromSettings(){
    	try{
	    	InputStreamReader isr = new InputStreamReader(getClass().getResourceAsStream("/settings.properties"));
	        BufferedReader br = new BufferedReader(isr);

	        int numLines = 0;
	        Vector<String> lines = new Vector<>();
	        while (true){
	        	String line = br.readLine();
	        	if(line == null) break;
	        	lines.add(line);
	        	numLines++;
	        }
	        
	        // screen resolution
	        String[] res = lines.get(0).split(" = ");
	        res = res[1].split(",");
	        res[0] = res[0].trim();
	        res[1] = res[1].trim();
	        width = Integer.parseInt(res[0]);
	        height = Integer.parseInt(res[1]);
	        
	        // window flags
	        String[] flags = lines.get(1).split(" = ");
	        flags = flags[1].split(","); 
	        for(int i=0; i < flags.length; i++){
	        	flags[i] = flags[i].trim();
	        	int flag = Integer.parseInt(flags[i]);
	        	this.flags |= flag;
	        }
	        
	        // FPS & UPS
	        String show = lines.get(2).split(" = ")[1];
	        show = show.trim();
	        if(show.equals("true")){
	        	showFpsUps = true;
	        }else{
	        	showFpsUps = false;
	        }
	        
	        lines.clear();
	        br.close();
    	}catch(IOException e){
    		ErrorLogger.log(e);
    	}
    }
    
}
