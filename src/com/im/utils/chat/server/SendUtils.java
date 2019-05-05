package com.im.utils.chat.server;
 

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class SendUtils {
	
	public static void pushMessage(ChannelHandlerContext cxt,String message) {
	    try {
	        ChannelFuture cf = cxt.writeAndFlush(message);
	        cf.addListener(new ChannelFutureListener() {
	            public void operationComplete(ChannelFuture future) {
	                if (future.isSuccess()) {
	                    //System.out.println("send success.");
	                } 
	            }
	        });
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}

	public static void pushMessage(ChannelHandlerContext chc,
			TextWebSocketFrame textWebSocketFrame) {
		try {
	        ChannelFuture cf = chc.writeAndFlush(textWebSocketFrame);
	        cf.addListener(new ChannelFutureListener() {
	            public void operationComplete(ChannelFuture future) {
	                if (future.isSuccess()) {
	                    //System.out.println("send success.");
	                } 
	            }
	        });
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
		
	}
}
