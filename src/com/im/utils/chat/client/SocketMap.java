package com.im.utils.chat.client;


import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SocketMap {
	public static Map<String,Socket> map=new ConcurrentHashMap<String, Socket>();

	public static void add(String key,Socket socket){
        map.put(key,socket);
    }
    public static Socket get(String key){
       return map.get(key);
    }
    @SuppressWarnings("rawtypes")
	public static String  getkey(Socket socket){
    	String key="";
    	for (Map.Entry entry:map.entrySet()){
            if (entry.getValue()==socket){
            	key=entry.getKey().toString();
            }
        }
    	 return key;
     }
    @SuppressWarnings("rawtypes")
	public static void removeByValue(Socket socket){
        for (Map.Entry entry:map.entrySet()){
            if (entry.getValue()==socket){
                map.remove(entry.getKey());
            }
        }
    }
    public static void remove(String key){
         map.remove(key);
            
    }
}
