package com.kingcoder.dungeon_crawler.items;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import com.kingcoder.dungeon_crawler.handlers.ErrorLogger;

public abstract class Item {
	 Item_ID item_id;
	 String name;
	 
	 public Item(String pathToFile){
		 readProperties(pathToFile);
	 }
	 
	 protected void readProperties(String pathToFile){
		 try {
			 InputStreamReader isr = new InputStreamReader(getClass().getResourceAsStream(pathToFile));
	         BufferedReader brInfo = new BufferedReader(isr);
	
	         int numLines = 0;
	         Vector<String> lines = new Vector<>();
	         while (true){
	        	 String line;
	        	 line = brInfo.readLine();
				
	             if(line == null) break;
	             numLines++;
	             lines.add(line);
	         }
	         
	         // initializing item_ID-s
	         String itemID = lines.get(0);
	         switch(itemID){
	         case "weapon":
	        	 item_id = Item_ID.weapon;
	        	 break;
	        	 
	         case "armor":
	        	 item_id = Item_ID.armor;
	        	 break;
	        	 
	         case "potion":
	        	 item_id = Item_ID.potion;
	        	 break;
	        	 
	         case "poison":
	        	 item_id = Item_ID.poison;
	        	 break;
	        	 
        	 default:
        		 System.out.println("ERR: NOT AN ITEM TYPE: " + itemID);
        		 return;
	         }	        
	         
		 } catch (IOException e) {
			 ErrorLogger.log(e);
		 }
	 }
}
