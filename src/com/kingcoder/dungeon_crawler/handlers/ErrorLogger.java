package com.kingcoder.dungeon_crawler.handlers;

public class ErrorLogger {

    public static void log(Exception e){
        e.printStackTrace();
        close();
    }
    
    public static void log(String message){
        System.out.println(message);
        close();
    }

    public static void close(){
        System.exit(1);
    }

}
