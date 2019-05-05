package com.im.utils.chat.common;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.im.model.chatfriend.Chatfriend;
import com.im.model.chatuser.Chatuser;
import com.im.service.chatfriend.IChatfriendService;
import com.im.service.chatuser.IChatuserService;

/**
 * @author lt
 * @version 1.0 
 */
@Component 
public abstract class CustomHeartbeatHandler extends SimpleChannelInboundHandler<Object> {
	 @Autowired  
	protected  static CustomHeartbeatHandler chh;
	@Autowired
	private IChatuserService iChatuserService;
	@Autowired
	private IChatfriendService iChatfriendService;
	
    public static final String PING_MSG = "{\"T\":\"0\"}";
    public static final String PONG_MSG = "{\"T\":\"0\"}";
    public static final String CUSTOM_MSG = "3";
    protected String name;

    public CustomHeartbeatHandler(String name) {
        this.name = name;
    }
    @PostConstruct
    public void init() {
    	chh = this;
    }
    @Override
    protected void channelRead0(ChannelHandlerContext context, Object string) throws Exception {
    	/*if (string.equals(PING_MSG)) {
            sendPongMsg(context);
        } else if (string.equals(PONG_MSG)){
        }
        else {
            handleData(context, string);
        }*/
    	handleData(context, string);
    }

    protected void sendPingMsg(ChannelHandlerContext context) {
        context.writeAndFlush(PING_MSG);
    }

    @SuppressWarnings("unused")
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

    
	@SuppressWarnings("rawtypes")
	@Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		final String userid=NettyChannelMap.getkey(ctx);
        //System.err.println(userid+"---" + ctx.channel().remoteAddress() + " is inactive---");
        for (Map.Entry entry:NettyChannelMap.map.entrySet()){
            if (entry.getValue()==ctx){
            	//database
                
                Chatuser chatuser= new Chatuser(); 
                chatuser.setUserid(entry.getKey().toString());
                chatuser.setIsonline("1");
                chatuser.setDetail("");
                //chatuser.setFlag("0");
                iChatuserService.updateChatuser(chatuser);
                
                Chatfriend chatfriend = new Chatfriend();
                chatfriend.setFriendid(entry.getKey().toString());
                chatfriend.setIsonline("1");
                iChatfriendService.updateChatfriend(chatfriend);
            }
            /*else{//通知其他用户自己下线
            	Chatuser chatuser= new Chatuser(); 
                chatuser=iChatuserService.selectchatuserById(userid);
                if(chatuser!=null){
                	ChannelHandlerContext temp = (ChannelHandlerContext) entry.getValue();
                	Map<String,String> paramMap=new HashMap<String,String>();
            		paramMap.put("T", "5");
            		paramMap.put("FI", userid);
            		paramMap.put("FN", chatuser.getUsername());
            		ObjectMapper mapper = new ObjectMapper();
        			String json = "";
        			json = mapper.writeValueAsString(paramMap);
                    String content = json;
                    temp.writeAndFlush(content+messageEnd);
                }
            }*/
        }
        
        
        ChannelFuture future = ctx.channel().close();
        future.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) {
                System.out.println(userid+"--退出成功！");
            }
        });
        
        //移除
        NettyChannelMap.remove(ctx);
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