package com.im.utils.chat.common;

import io.netty.channel.ChannelHandlerContext;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 * @author lt
 * @version 1.0 
 */
public class NettyChannelMap {
    public static Map<String,ChannelHandlerContext> map=new ConcurrentHashMap<String, ChannelHandlerContext>();
    public static Map<String,Long> timemap=new ConcurrentHashMap<String, Long>();
    
    public static void add(String clientId,ChannelHandlerContext channelHandlerContext){
        map.put(clientId,channelHandlerContext);
        timemap.put(clientId, new Date().getTime());
    }
    public static ChannelHandlerContext get(String clientId){
       return map.get(clientId);
    }
    @SuppressWarnings("rawtypes")
	public static String  getkey(ChannelHandlerContext channelHandlerContext){
    	String key="";
    	for (Map.Entry entry:map.entrySet()){
            if (entry.getValue()==channelHandlerContext){
            	key=entry.getKey().toString();
            }
        }
    	 return key;
     }
    @SuppressWarnings("rawtypes")
	public static void remove(ChannelHandlerContext channelHandlerContext){
        for (Map.Entry entry:map.entrySet()){
            if (entry.getValue()==channelHandlerContext){
                map.remove(entry.getKey());
                timemap.remove(entry.getKey());
            }
        }
    } 
    public static void removeByKey(String key){
        ChannelHandlerContext ctx=map.get(key);
        ctx.close();
        map.remove(key);
        timemap.remove(key);
    }

}
