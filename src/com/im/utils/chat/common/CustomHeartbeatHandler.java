package com.im.utils.chat.common;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.im.model.chataddress.Chataddress;
import com.im.model.chatfriend.Chatfriend;
import com.im.model.chatuser.Chatuser;
import com.im.service.chataddress.IChataddressService;
import com.im.service.chatfriend.IChatfriendService;
import com.im.service.chatuser.IChatuserService;
import com.im.utils.chat.RedisUtil;
import com.im.utils.chat.ServerManager;

/**
 * @author lt
 * @version 1.0 
 */
@Component 
public abstract class CustomHeartbeatHandler extends SimpleChannelInboundHandler<Object> {
	
	private static CustomHeartbeatHandler chh;
	@Autowired
	private IChatuserService iChatuserService;
	@Autowired
	private IChataddressService iChataddressService;
	@Autowired
	private IChatfriendService iChatfriendService;
	
    public static final String PING_MSG = "{\"T\":\"0\"}";
    public static final String PONG_MSG = "{\"T\":\"0\"}";
    public static final String CUSTOM_MSG = "3";
    protected String name;
    private int heartbeatCount = 0;

    public CustomHeartbeatHandler(String name) {
        this.name = name;
    }
    @PostConstruct
    public void init() {
    	chh = this;
    }
    @Override
    protected void channelRead0(ChannelHandlerContext context, Object string) throws Exception {
    	//System.out.println(context.name()+"|"+string);
    	if (string.equals(PING_MSG)) {
            sendPongMsg(context);
        } else if (string.equals(PONG_MSG)){
            System.out.println(name + " get pong msg from " + context.channel().remoteAddress());
        }
        else {
            handleData(context, string);
            //System.out.println(context.name()+"|");
        }
    }

    protected void sendPingMsg(ChannelHandlerContext context) {
        /*ByteBuf buf = context.alloc().buffer(5);
        buf.writeInt(5);
        buf.writeByte(PING_MSG);
        buf.retain();
        context.writeAndFlush(buf);*/
        context.writeAndFlush(PING_MSG);
        heartbeatCount++;
        System.out.println(name + " sent ping msg to " + context.channel().remoteAddress() + ", count: " + heartbeatCount);
    }

    private void sendPongMsg(ChannelHandlerContext context) {
        /*ByteBuf buf = context.alloc().buffer(5);
        buf.writeInt(5);
        buf.writeByte(PONG_MSG);
        context.channel().writeAndFlush(buf);*/
        context.channel().writeAndFlush(PONG_MSG);
        heartbeatCount++;
        System.out.println(name + " sent pong msg to " + context.channel().remoteAddress() + ", count: " + heartbeatCount);
    }

   //protected abstract void handleData(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf);
    protected abstract void handleData(ChannelHandlerContext channelHandlerContext, Object content);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // IdleStateHandler 所产生的 IdleStateEvent 的处理逻辑.
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
    	
        //System.err.println("---" + ctx.channel().remoteAddress() + " is active---");
     
    }

    @SuppressWarnings("rawtypes")
	@Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        
        String userid=NettyChannelMap.getkey(ctx);
        for (Map.Entry entry:NettyChannelMap.map.entrySet()){
            if (entry.getValue()==ctx){
            	//database
                Chatuser chatuser= new Chatuser(); 
                chatuser.setUserid(entry.getKey().toString());
                chatuser.setIsonline("1");
                chatuser.setDetail("");
                //chatuser.setFlag("0");
                chh.iChatuserService.updateChatuser(chatuser);
                
                Chatfriend chatfriend = new Chatfriend();
                chatfriend.setFriendid(entry.getKey().toString());
                chatfriend.setIsonline("1");
                chh.iChatfriendService.updateChatfriend(chatfriend);
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
                    ByteBuf buf = temp.alloc().buffer(5 + content.getBytes().length);
                    buf.writeInt(5 + content.getBytes().length);
                    buf.writeByte(CustomHeartbeatHandler.CUSTOM_MSG);
                    buf.writeBytes(content.getBytes());
                    temp.writeAndFlush(buf);
                    temp.writeAndFlush(content);
                	 
                }
            	
            }*/
        }
        //移除
        NettyChannelMap.remove(ctx);
        if(ServerManager.cacheType.equals("redis")){
        	RedisUtil.delOject(userid);
        }
        else if(ServerManager.cacheType.equals("database")){
        	Chataddress ca = new Chataddress();
        	ca.setUserid(userid);
        	ca.setStatus("1");
        	chh.iChataddressService.updateChatByUserid(ca);
        }
        
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