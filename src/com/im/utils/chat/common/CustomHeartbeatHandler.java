package com.im.utils.chat.common;

import java.util.Date;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

/**
 * @author lt
 * @version 1.0 
 */
@Component 
public abstract class CustomHeartbeatHandler extends SimpleChannelInboundHandler<Object> {
 
	
    public static final String PING_MSG = "{\"T\":\"0\"}";
    public static final String PONG_MSG = "{\"T\":\"0\"}";
    public static final String CUSTOM_MSG = "3";
    protected String name;

    public CustomHeartbeatHandler(String name) {
        this.name = name;
    }
    @PostConstruct
    public void init() {
    	 
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object message) throws Exception {
    	//这里先不判断websocket了
       	if (message.toString().trim().equals(PING_MSG)) {
            sendPongMsg(ctx);
           //记录时间
           NettyChannelMap.timemap.put(NettyChannelMap.getkey(ctx), new Date().getTime());
        } else if (message.equals(PONG_MSG)){
        	//do  noting 
        }
        else {
            handleData(ctx, message);
        } 
    }

    protected void sendPingMsg(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(PING_MSG);
    }

	private void sendPongMsg(ChannelHandlerContext ctx) {
        ctx.channel().writeAndFlush(PONG_MSG);
    }

    protected abstract void handleData(ChannelHandlerContext ctx, Object message);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case READER_IDLE:
                    handleReaderIdle(ctx);
                    break;
                case WRITER_IDLE:
                    handleWriterIdle(ctx);
                    break;
                case ALL_IDLE:
                    handleAllIdle(ctx);
                    break;
                default:
                    break;
            }
        }
    }
 
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
     
    }
 

    protected void handleReaderIdle(ChannelHandlerContext ctx) {
        System.err.println("---READER_IDLE---");
    }

    protected void handleWriterIdle(ChannelHandlerContext ctx) {
        System.err.println("---WRITER_IDLE---");
    }

    protected void handleAllIdle(ChannelHandlerContext ctx) {
        System.err.println("---ALL_IDLE---");
    }
}