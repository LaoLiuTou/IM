package com.im.utils.chat.server;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import javapns.devices.Device;
import javapns.devices.implementations.basic.BasicDevice;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;

import javax.annotation.PostConstruct;

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
 * @author LT
 * @version 1.0
 * @date 2014年2月14日
 */
@Component
public class WebSocketServerHandler extends CustomHeartbeatHandler {
	Logger logger = Logger.getLogger("IMLogger");

	private WebSocketServerHandshaker handshaker;
	private static WebSocketServerHandler wssh;

	@Autowired
	private IChatuserService iChatuserService;
	@Autowired
	private IChatmessageService iChatmessageService;
	@Autowired
	private IChatfriendService iChatfriendService;
	@Autowired
	private IChataddressService iChataddressService;

	public WebSocketServerHandler() {
		super("server");
	}
	// 3.容器初始化的时候进行执行-这里是重点
	@PostConstruct
	public void init() {
		wssh = this;
	}

	/*@Override
	public void channelRead0(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		// 传统的HTTP接入
		if (msg instanceof FullHttpRequest) {
			handleHttpRequest(ctx, (FullHttpRequest) msg);
		}
		// WebSocket接入
		else if (msg instanceof WebSocketFrame) {
			handleWebSocketFrame(ctx, (WebSocketFrame) msg);
		}
	}*/
	@Override
	protected void handleData(ChannelHandlerContext ctx, Object msg) {
		  
		// 传统的HTTP接入
		if (msg instanceof FullHttpRequest) {
			try {
				handleHttpRequest(ctx, (FullHttpRequest) msg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// WebSocket接入
		else if (msg instanceof WebSocketFrame) {
			handleWebSocketFrame(ctx, (WebSocketFrame) msg);
		}
		
	}
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	private void handleHttpRequest(ChannelHandlerContext ctx,
			FullHttpRequest req) throws Exception {

		// 如果HTTP解码失败，返回HHTP异常
		if (!req.decoderResult().isSuccess()
				|| (!"websocket".equals(req.headers().get("Upgrade")))) {
			sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1,
					BAD_REQUEST));
			return;
		}

		String socketIp = "", socketPort = "";
		try {
			Properties props = new Properties();

			props.load(RedisUtil.class.getClassLoader().getResourceAsStream(
					"socket/socket.properties"));

			socketIp = props.getProperty("ip").trim();
			socketPort = props.getProperty("port").trim();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 构造握手响应返回，本机测试
		WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
				"ws://" + socketIp + ":" + socketPort, null, false);
		handshaker = wsFactory.newHandshaker(req);
		if (handshaker == null) {
			WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx
					.channel());
		} else {
			handshaker.handshake(ctx.channel(), req);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked"  })
	private void handleWebSocketFrame(
			ChannelHandlerContext ctx, WebSocketFrame frame) {

		// 判断是否是关闭链路的指令
		if (frame instanceof CloseWebSocketFrame) {
			handshaker.close(ctx.channel(),
					(CloseWebSocketFrame) frame.retain());

			String userid = NettyChannelMap.getkey(ctx);
			if (userid.length() > 0) {
				// database
				Chatuser chatuser = new Chatuser();
				chatuser.setUserid(userid);
				chatuser.setIsonline("1");
				chatuser.setDetail("");
				// chatuser.setFlag("0");
				wssh.iChatuserService.updateChatuser(chatuser);

				Chatfriend chatfriend = new Chatfriend();
				chatfriend.setFriendid(userid);
				chatfriend.setIsonline("1");
				wssh.iChatfriendService.updateChatfriend(chatfriend);

				if (ServerManager.cacheType.equals("redis")) {
					RedisUtil.delOject(userid);
				} else if (ServerManager.cacheType.equals("database")) {
					Chataddress ca = new Chataddress();
					ca.setUserid(userid);
					ca.setStatus("1");
					wssh.iChataddressService.updateChatByUserid(ca);
				}
			}

			// 移除
			NettyChannelMap.remove(ctx);
			ctx.close();
			return;
		}
		// 判断是否是Ping消息
		if (frame instanceof PingWebSocketFrame) {
			ctx.channel().write(
					new PongWebSocketFrame(frame.content().retain()));
			return;
		}
		// 本例程仅支持文本消息，不支持二进制消息
		if (!(frame instanceof TextWebSocketFrame)) {
			throw new UnsupportedOperationException(String.format(
					"%s frame types not supported", frame.getClass().getName()));
		}

		// 返回应答消息
		String request = ((TextWebSocketFrame) frame).text();
		// logger.info(String.format("%s received %s",
		// ctx.channel(), request));
		// ctx.writeAndFlush(new
		// TextWebSocketFrame("欢迎使用Netty WebSocket服务，现在时刻："
		// + new java.util.Date().toString()));
		// new TextWebSocketFrame("欢迎使用Netty WebSocket服务，现在时刻："
		// + new java.util.Date().toString()));
		try {
			/*
			 * byte[] data = new byte[buf.readableBytes() - 5]; ByteBuf
			 * responseBuf = Unpooled.copiedBuffer(buf); buf.skipBytes(5);
			 * buf.readBytes(data); String content = new String(data);
			 */
			String content = request;
			//
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
					SendUtils.pushMessage(tempCtx,new TextWebSocketFrame(replayContent));
					//
					if (ServerManager.cacheType.equals("redis")) {
						RedisUtil.delOject(map.get("UI"));
					} else if (ServerManager.cacheType.equals("database")) {
						Chataddress ca = new Chataddress();
						ca.setUserid(map.get("UI"));
						ca.setStatus("1");
						wssh.iChataddressService.updateChatByUserid(ca);
					}

				}

				// database run
				try {
					NettyChannelMap.add(map.get("UI"), ctx);
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

					if (wssh.iChatuserService.selectChatuserById(map.get("UI")) == null) {
						wssh.iChatuserService.addChatuser(chatuser);
					} else {
						wssh.iChatuserService.updateChatuser(chatuser);
					}

					Chatfriend chatfriend = new Chatfriend();
					chatfriend.setFriendid(map.get("UI"));
					chatfriend.setIsonline("0");
					wssh.iChatfriendService.updateChatfriend(chatfriend);

					// 发送离线消息
					new MessageUtil().sendOfflineMessage(ctx,
							map.get("UI"), "websocket");

					// 通知上线
					/*
					 * String userid=map.get("UI"); String
					 * username=map.get("UN"); if(userid.length()>0){
					 * sendExceptMessage(ctx,
					 * "4",userid,username); }
					 */
					SendUtils.pushMessage(ctx,new TextWebSocketFrame(replayContent));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					replayContent = "{\"T\":\"1\",\"R\":\"1\"}";// 登录失败
					SendUtils.pushMessage(ctx,new TextWebSocketFrame(replayContent));
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
					List<Chataddress> list = wssh.iChataddressService
							.selectChataddressByParam(paramMap);
					if (list.size() > 0) {
						Chataddress ca = new Chataddress();
						ca.setUserid(map.get("UI"));
						ca.setAddress(socketIp + ":" + socketPort);
						ca.setStatus("0");
						wssh.iChataddressService.updateChatByUserid(ca);
					} else {
						Chataddress ca = new Chataddress();
						ca.setUserid(map.get("UI"));
						ca.setAddress(socketIp + ":" + socketPort);
						ca.setStatus("0");
						wssh.iChataddressService.addChataddress(ca);
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
					wssh.iChatuserService.updateChatuser(chatuser);

					Chatfriend chatfriend = new Chatfriend();
					chatfriend.setFriendid(map.get("UI"));
					chatfriend.setIsonline("1");
					wssh.iChatfriendService.updateChatfriend(chatfriend);

					/*
					 * String
					 * userid=NettyChannelMap.getkey(ctx);
					 * String username=map.get("UN"); if(userid.length()>0){
					 * //通知下线 sendExceptMessage(ctx,
					 * "5",userid,username); }
					 */

					NettyChannelMap.remove(ctx);
					SendUtils.pushMessage(ctx,new TextWebSocketFrame(replayContent));
					ctx.close();

					if (ServerManager.cacheType.equals("redis")) {
						RedisUtil.delOject(map.get("UI"));
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					replayContent = "{\"T\":\"2\",\"R\":\"1\"}";// 退出失败
					SendUtils.pushMessage(ctx,new TextWebSocketFrame(replayContent));
					e.printStackTrace();
				}
				logger.info("用户退出，ID:" + map.get("UI") + "；用户名："
						+ map.get("UN"));
			} else if (map.get("T").equals("3") || map.get("T").equals("7")) {// text
				// 判断消息类型 查询group中的用户信息 循环发送消息
				ChannelHandlerContext chc = NettyChannelMap.get(map.get("FI"));
				new MessageUtil().sendOnlineMessage(chc, map, "websocket");

			} else if (map.get("T").equals("11")) {// 已读
				if (map.get("MI") != null) {
					String[] messageIds = map.get("MI").split("\\|");
					for (String mi : messageIds) {
						Chatmessage cm = wssh.iChatmessageService
								.selectChatmessageById(mi);
						if (cm != null) {
							cm.setReadstatus("0");
							wssh.iChatmessageService.updateChatmessage(cm);
						}
					}
				}
			} else if (map.get("T").equals("0")) {
				SendUtils.pushMessage(ctx,new TextWebSocketFrame("{\"T\":\"0\"}"));
				//记录时间
		        NettyChannelMap.timemap.put(NettyChannelMap.getkey(ctx), new Date().getTime());
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.info("推送异常:" + e.toString());
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	private static void sendHttpResponse(ChannelHandlerContext ctx,
			FullHttpRequest req, FullHttpResponse res) {
		// 返回应答给客户端
		if (res.getStatus().code() != 200) {
			ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(),
					CharsetUtil.UTF_8);
			res.content().writeBytes(buf);
			buf.release();
			HttpUtil.setContentLength(res, res.content().readableBytes());
		}

		// 如果是非Keep-Alive，关闭连接
		ChannelFuture f = ctx.channel().writeAndFlush(res);
		if (!HttpUtil.isKeepAlive(req) || res.status().code() != 200) {
			f.addListener(ChannelFutureListener.CLOSE);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	/**
	 * 注册到redis
	 * 
	 * @return
	 */
	private void addToRedis(String id) {

		String socketIp = ServerManager.socketIp;
		String socketPort = ServerManager.socketPort;

		RedisUtil.setObject(id, socketIp + ":" + socketPort);

	}
	
}