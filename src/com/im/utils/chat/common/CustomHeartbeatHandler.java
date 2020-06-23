package com.im.utils.chat.common;

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
    protected void channelRead0(ChannelHandlerContext context, Object string) throws Exception {
    	if (string.toString().trim().equals(PING_MSG)) {
            sendPongMsg(context);
        } else if (string.equals(PONG_MSG)){
        }
        else {
            handleData(context, string);
        }
    	//shandleData(context, string);
    }

    protected void sendPingMsg(ChannelHandlerContext context) {
        context.writeAndFlush(PING_MSG);
    }

	private void sendPongMsg(ChannelHandlerContext context) {
        context.channel().writeAndFlush(PONG_MSG);
    }

    protected abstract void handleData(ChannelHandlerContext channelHandlerContext, Object content);

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