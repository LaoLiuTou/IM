package com.im.utils.chat.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javapns.devices.Device;
import javapns.devices.implementations.basic.BasicDevice;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.im.model.chataddress.Chataddress;
import com.im.model.chatfriend.Chatfriend;
import com.im.model.chatmessage.Chatmessage;
import com.im.model.chatuser.Chatuser;
import com.im.service.chataddress.IChataddressService;
import com.im.service.chatfriend.IChatfriendService;
import com.im.service.chatmessage.IChatmessageService;
import com.im.service.chatuser.IChatuserService;
import com.im.utils.chat.ApnsTools;
import com.im.utils.chat.RedisUtil;
import com.im.utils.chat.ServerManager;
import com.im.utils.chat.client.SocketClient;
import com.im.utils.chat.common.CustomHeartbeatHandler;
import com.im.utils.chat.common.NettyChannelMap;

/**
 * @author lt
 * @version 1.0
 */
@Component
public class SocketServerHandler extends CustomHeartbeatHandler {
	private static SocketServerHandler ssh;
	@Autowired
	private IChatuserService iChatuserService;
	@Autowired
	private IChatfriendService iChatfriendService;
	@Autowired
	private IChatmessageService iChatmessageService;
	@Autowired
	private IChataddressService iChataddressService;
	Logger logger = Logger.getLogger("IMLogger");

	public SocketServerHandler() {
		super("server");
	}

	@PostConstruct
	public void init() {
		ssh = this;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void handleData(ChannelHandlerContext channelHandlerContext,
			Object msg) {
		try {
			/*
			 * byte[] data = new byte[buf.readableBytes() - 5]; ByteBuf
			 * responseBuf = Unpooled.copiedBuffer(buf); buf.skipBytes(5);
			 * buf.readBytes(data); String content = new String(data);
			 */
			String content = msg.toString();
			ObjectMapper mapper = new ObjectMapper();
			Map<String, String> map = new HashMap<String, String>();
			map = mapper.readValue(content,
					new TypeReference<Map<String, String>>() {
					});

			if (map.get("T").equals("1")) {// login
				// 登录
				String replayContent = "";
				if (NettyChannelMap.get(map.get("UI")) != null) {
					// 被踢下线
					replayContent = "{\"T\":\"6\",\"R\":\"0\"}";// 被踢下线
					ChannelHandlerContext tempCtx = NettyChannelMap.get(map
							.get("UI"));
					tempCtx.writeAndFlush(replayContent);
					tempCtx.close();
					NettyChannelMap.remove(NettyChannelMap.get(map.get("UI")));
					if (ServerManager.cacheType.equals("redis")) {
						RedisUtil.delOject(map.get("UI"));
					} else if (ServerManager.cacheType.equals("database")) {
						Chataddress ca = new Chataddress();
						ca.setUserid(map.get("UI"));
						ca.setStatus("1");
						ssh.iChataddressService.updateChatByUserid(ca);
					}
				}
				// database run
				try {
					NettyChannelMap.add(map.get("UI"), channelHandlerContext);
					replayContent = "{\"T\":\"1\",\"R\":\"0\"}";// 登录成功
					Chatuser chatuser = new Chatuser();
					chatuser.setUserid(map.get("UI"));
					chatuser.setUsername(map.get("UN"));
					chatuser.setUserimage(map.get("UH"));
					chatuser.setAddtime(new Date());
					chatuser.setIsonline("0");
					// 是否是ios设备 存token
					if (map.containsKey("TK")) {
						chatuser.setDetail(map.get("TK"));
						chatuser.setFlag("1");// 0-安卓 1-ios
					} else {
						chatuser.setDetail("");
						chatuser.setFlag("0");// 0-安卓 1-ios
					}
					if (ssh.iChatuserService.selectChatuserById(map.get("UI")) == null) {
						ssh.iChatuserService.addChatuser(chatuser);
					} else {
						ssh.iChatuserService.updateChatuser(chatuser);
					}
					Chatfriend chatfriend = new Chatfriend();
					chatfriend.setFriendid(map.get("UI"));
					chatfriend.setIsonline("0");
					ssh.iChatfriendService.updateChatfriend(chatfriend);

					// 发送离线消息
					new MessageUtil().sendOfflineMessage(channelHandlerContext,
							map.get("UI"),"socket");

					// 通知上线
					/*
					 * String userid=map.get("UI"); String
					 * username=map.get("UN"); if(userid.length()>0){
					 * sendExceptMessage(channelHandlerContext,
					 * "4",userid,username); }
					 */
					channelHandlerContext.writeAndFlush(replayContent);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					replayContent = "{\"T\":\"1\",\"R\":\"1\"}";// 登录失败
					channelHandlerContext.writeAndFlush(replayContent);
					e.printStackTrace();
				}

				if (ServerManager.cacheType.equals("redis")) {
					addToRedis(map.get("UI"));
				} else if (ServerManager.cacheType.equals("database")) {
					String socketIp = ServerManager.socketIp;
					String socketPort = ServerManager.socketPort;

					Map paramMap = new HashMap();
					paramMap.put("fromPage", 0);
					paramMap.put("toPage", 1);
					paramMap.put("orderBy", "ID DESC");
					paramMap.put("userid", map.get("UI"));
					List<Chataddress> list = ssh.iChataddressService
							.selectChataddressByParam(paramMap);
					if (list.size() > 0) {
						Chataddress ca = new Chataddress();
						ca.setUserid(map.get("UI"));
						ca.setAddress(socketIp + ":" + socketPort);
						ca.setStatus("0");
						ssh.iChataddressService.updateChatByUserid(ca);
					} else {
						Chataddress ca = new Chataddress();
						ca.setUserid(map.get("UI"));
						ca.setAddress(socketIp + ":" + socketPort);
						ca.setStatus("0");
						ssh.iChataddressService.addChataddress(ca);
					}
				}
				logger.info("用户登录，ID:" + map.get("UI") + "；用户名："
						+ map.get("UN"));
			} else if (map.get("T").equals("2")) {// logout

				String replayContent = "{\"T\":\"2\",\"R\":\"0\"}";// 退出成功
				// database
				try {
					Chatuser chatuser = new Chatuser();
					chatuser.setUserid(map.get("UI"));
					chatuser.setIsonline("1");
					chatuser.setDetail("");
					chatuser.setFlag("0");
					ssh.iChatuserService.updateChatuser(chatuser);

					Chatfriend chatfriend = new Chatfriend();
					chatfriend.setFriendid(map.get("UI"));
					chatfriend.setIsonline("1");
					ssh.iChatfriendService.updateChatfriend(chatfriend);

					/*
					 * String
					 * userid=NettyChannelMap.getkey(channelHandlerContext);
					 * String username=map.get("UN"); if(userid.length()>0){
					 * //通知下线 
					 * MessageUtil.sendExceptMessage(channelHandlerContext,
					 * "5",userid,username); }
					 */

					NettyChannelMap.remove(channelHandlerContext);
					channelHandlerContext.writeAndFlush(replayContent);
					channelHandlerContext.close();

					if (ServerManager.cacheType.equals("redis")) {
						RedisUtil.delOject(map.get("UI"));
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					replayContent = "{\"T\":\"2\",\"R\":\"1\"}";// 退出失败
					channelHandlerContext.writeAndFlush(replayContent);
					channelHandlerContext.close();
					e.printStackTrace();
				}
				logger.info("用户退出，ID:" + map.get("UI") + "；用户名："
						+ map.get("UN"));
			} else if (map.get("T").equals("3") || map.get("T").equals("7")) {// text
				// 判断消息类型 查询group中的用户信息 循环发送消息 
				ChannelHandlerContext chc = NettyChannelMap.get(map.get("FI"));
				new MessageUtil().sendOnlineMessage(chc,map,"socket");
			}
			else if(map.get("T").equals("11")){//已读
				if(map.get("MI")!=null){
					String[] messageIds =  map.get("MI").split("\\|");
					for(String mi:messageIds){
						Chatmessage cm = ssh.iChatmessageService.selectChatmessageById(mi);
						if(cm!=null){
							cm.setReadstatus("0");
							ssh.iChatmessageService.updateChatmessage(cm);
						}
					}
					
				}
			}
			else if(map.get("T").equals("0")){
				channelHandlerContext.writeAndFlush("{\"T\":\"0\"}");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.info("推送异常:" + e.toString());
			e.printStackTrace();
		}
	}

	@Override
	protected void handleReaderIdle(ChannelHandlerContext ctx) {
		super.handleReaderIdle(ctx);

		String userid = NettyChannelMap.getkey(ctx);
		if (userid.length() > 0) {
			// database
			Chatuser chatuser = new Chatuser();
			chatuser.setUserid(userid);
			chatuser.setIsonline("1");
			chatuser.setDetail("");
			// chatuser.setFlag("0");
			ssh.iChatuserService.updateChatuser(chatuser);

			Chatfriend chatfriend = new Chatfriend();
			chatfriend.setFriendid(userid);
			chatfriend.setIsonline("1");
			ssh.iChatfriendService.updateChatfriend(chatfriend);
			// 通知下线
			/*
			 * chatuser= new Chatuser();
			 * chatuser=ssh.iChatuserService.selectchatuserById(userid);
			 * if(chatuser!=null){
			 * 
			 * MessageUtil.sendExceptMessage(ctx,
			 * "5",userid,chatuser.getUsername()); }
			 */
		}

		// 移除
		NettyChannelMap.remove(ctx);

		ctx.close();
	}
	@SuppressWarnings("rawtypes")
	@Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		String userid=NettyChannelMap.getkey(ctx);
        for (Map.Entry entry:NettyChannelMap.map.entrySet()){
            if (entry.getValue()==ctx){
            	//database
                Chatuser chatuser= new Chatuser(); 
                chatuser.setUserid(entry.getKey().toString());
                chatuser.setIsonline("1");
                //chatuser.setDetail("");
                //chatuser.setFlag("0");
                ssh.iChatuserService.updateChatuser(chatuser);
                
                Chatfriend chatfriend = new Chatfriend();
                chatfriend.setFriendid(entry.getKey().toString());
                chatfriend.setIsonline("1");
                ssh.iChatfriendService.updateChatfriend(chatfriend);
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
        	ssh.iChataddressService.updateChatByUserid(ca);
        }
	}
	/**
	 * 注册到redis
	 * 
	 * @return
	 */
	private void addToRedis(String id) {
		// 参数

		String socketIp = ServerManager.socketIp;
		String socketPort = ServerManager.socketPort;

		RedisUtil.setObject(id, socketIp + ":" + socketPort);

	}
	// {"T":"1","UI":"101","UN":"用户名","UH":""}
	// {"T":"3","CT":"dQ==\n","UI":"100","UN":"用户名999","FI":"3294","TP":"0","HI":""}
}