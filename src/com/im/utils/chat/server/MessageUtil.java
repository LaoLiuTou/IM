package com.im.utils.chat.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import javapns.devices.Device;
import javapns.devices.implementations.basic.BasicDevice;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.im.model.chatfriend.Chatfriend;
import com.im.model.chatmessage.Chatmessage;
import com.im.model.chatuser.Chatuser; 
import com.im.service.chatfriend.IChatfriendService;
import com.im.service.chatmessage.IChatmessageService;
import com.im.service.chatuser.IChatuserService;
import com.im.utils.chat.ApnsTools;
import com.im.utils.chat.common.NettyChannelMap;
@Component
public class MessageUtil {
	private static MessageUtil mu;
	@Autowired
	private  IChatuserService iChatuserService;
	@Autowired
	private  IChatmessageService iChatmessageService;
	@Autowired
	private  IChatfriendService iChatfriendService;

	@PostConstruct
	public void init() {
		mu = this;
	}
	/**
	 * 发送在线消息
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes", "static-access" })
	public  void sendOnlineMessage(ChannelHandlerContext chc,
			Map<String, String> map,String type) {
		Logger logger = Logger.getLogger("IMLogger");
		ObjectMapper mapper = new ObjectMapper();
		try {
			if (chc != null) {
				// 保存在线消息
				String messageId = saveOnlineMessage(map);
				map.put("MI", messageId);
				if(type.equals("socket")){
					SendUtils.pushMessage(chc,mapper.writeValueAsString(map));
				}
				else if(type.equals("websocket")){
					SendUtils.pushMessage(chc,new TextWebSocketFrame(mapper.writeValueAsString(map)));
				}
			} else {
			 
				Chatuser chatuser = mu.iChatuserService.selectChatuserById(map
						.get("FI"));
				if (chatuser != null && chatuser.getFlag() != null
						&& chatuser.getFlag().equals("1")) {// ios
					// ios push
					String tokenStr = chatuser.getDetail();
					tokenStr = tokenStr.replace("<", "").replace(">", "");
					List<String> tokens = new ArrayList<String>();
					tokens.add(tokenStr);
					// 未读数
					Map param = new HashMap();
					param.put("userid", map.get("FI"));
					param.put("readstatus", "1");
					// param.put("sendstatus", "0");
					param.put("chattype", "1");
					int count = mu.iChatmessageService
							.selectCountChatmessageByParam(param);
					// Integer count=1;
					boolean sendCount = true;
					try {
						String message = "{'aps':{'alert':'你有一条新消息！'}}";
						// message是一个json的字符串{“aps”:{“alert”:”iphone推送测试”}}
						PushNotificationPayload payLoad = PushNotificationPayload
								.fromJSON(message);
						payLoad.addAlert("你有一条新消息！"); // 消息内容
						payLoad.addBadge(count + 1); // iphone应用图标上小红圈上的数值
						payLoad.addSound("default"); // 铃音 默认

						List<PushedNotification> notifications = new ArrayList<PushedNotification>();
						// 发送push消息
						if (sendCount) {
							Device device = new BasicDevice();
							device.setToken(tokens.get(0));
							PushedNotification notification = new ApnsTools().pushManager
									.sendNotification(device, payLoad, true);
							notifications.add(notification);
						} else {
							List<Device> device = new ArrayList<Device>();
							for (String token : tokens) {
								device.add(new BasicDevice(token));
							}
							notifications = new ApnsTools().pushManager
									.sendNotifications(payLoad, device);
						}
						List<PushedNotification> failedNotifications = PushedNotification
								.findFailedNotifications(notifications);
						List<PushedNotification> successfulNotifications = PushedNotification
								.findSuccessfulNotifications(notifications);
						int failed = failedNotifications.size();
						int successful = successfulNotifications.size();
						if (successful > 0 && failed == 0) {
							logger.info("IOS:推送成功"
									+ failedNotifications.toString());
						} else {
							logger.info("IOS:推送失败"
									+ failedNotifications.toString());
						}
						// 保存离线消息
						saveOfflineMessage(map);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {// 安卓
						// 保存离线消息
					saveOfflineMessage(map);
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 发送离线消息
	 * 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
	public  void sendOfflineMessage(ChannelHandlerContext ctx,
			String userId,String type) {
		try {
			// database
			// Thread.sleep(5000);
			Map param = new HashMap();
			param.put("fromPage", 0);
			param.put("toPage", 100);
			param.put("userid", userId);
			// param.put("readstatus", "1");
			param.put("sendstatus", "1");
			param.put("chattype", "1");
			List<Chatmessage> list = mu.iChatmessageService
					.selectChatmessageByParam(param);
			// for (Chatmessage cm : list) {
			for (int index = list.size() - 1; index >= 0; index--) {
				Chatmessage cm = list.get(index);

				Map<String, String> paramMap = new HashMap<String, String>();

				paramMap.put("T", cm.getFlag());
				paramMap.put("FI", cm.getUserid());
				paramMap.put("UI", cm.getFriendid());
				paramMap.put("CT", cm.getContent());
				paramMap.put("TP", cm.getContenttype());
				paramMap.put("CF", cm.getFlag());
				paramMap.put("MI", cm.getId() + "");

				Map userparam = new HashMap();
				userparam.put("fromPage", 0);
				userparam.put("toPage", 100);
				userparam.put("userid", cm.getFriendid());

				List<Chatuser> chatuserTemp = mu.iChatuserService
						.selectChatuserByParam(userparam);
				Chatuser chatuser = null;
				if (chatuserTemp.size() > 0) {
					chatuser = chatuserTemp.get(0);
				}
				// Chatuser
				// chatuser=iChatuserService.selectchatuserById(cm.getFriendid());
				if (chatuser != null) {
					paramMap.put("UN", chatuser.getUsername());
					paramMap.put("UH", chatuser.getUserimage());
				} else {
					paramMap.put("UN", "");
					paramMap.put("UH", "");
				}

				ObjectMapper mapper = new ObjectMapper();
				String json = "";
				json = mapper.writeValueAsString(paramMap);
				String content = json;
				if(type.equals("socket")){
					SendUtils.pushMessage(ctx,content);
				}
				else if(type.equals("websocket")){
					SendUtils.pushMessage(ctx,new TextWebSocketFrame(content));
				}
				

				// 更新消息状态
				// cm.setReadstatus("0");
				cm.setSendstatus("0");
				mu.iChatmessageService.updateChatmessage(cm);

				Thread.sleep(1000);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	/**
	 * 保存在线消息
	 * 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
	public  String saveOnlineMessage(Map<String, String> map) {
		String messageId = "";
		try {

			// 发送消息
			Chatmessage chatmessage = new Chatmessage();
			chatmessage.setUserid(map.get("UI"));
			chatmessage.setFriendid(map.get("FI"));
			chatmessage.setChattype("0");// 发送0--接收1
			chatmessage.setAddtime(new Date());
			chatmessage.setIsgroup("0");// 个人0--分组1
			chatmessage.setContenttype(map.get("TP"));// 0-文本，1-图片，2-语音，3-视频，4-文件
			chatmessage.setContent(map.get("CT"));
			chatmessage.setReadstatus("0");// 是否已读 0-yes 1-no
			chatmessage.setSendstatus("0");// 是否发送成功 0-yes 1-no
			chatmessage.setFlag(map.get("T"));// 是否推送 1-yes 0-no
			mu.iChatmessageService.addChatmessage(chatmessage);
			// 接收消息
			chatmessage.setUserid(map.get("FI"));
			chatmessage.setFriendid(map.get("UI"));
			chatmessage.setReadstatus("1");// 是否已读 0-yes 1-no
			chatmessage.setChattype("1");// 发送0--接收1
			mu.iChatmessageService.addChatmessage(chatmessage);
			messageId = chatmessage.getId() + "";
			Map param = new HashMap();
			param.put("fromPage", 0);
			param.put("toPage", 1);
			param.put("friendid", map.get("FI"));
			param.put("userid", map.get("UI"));
			if (mu.iChatfriendService.selectChatfriendByParam(param).size() > 0) {

			} else {
				Chatfriend chatfriend = new Chatfriend();
				chatfriend.setUserid(map.get("UI"));
				chatfriend.setIsgroup("1");
				chatfriend.setFriendid(map.get("FI"));
				chatfriend.setIsonline("0");
				mu.iChatfriendService.addChatfriend(chatfriend);
				chatfriend.setUserid(map.get("FI"));
				chatfriend.setFriendid(map.get("UI"));
				mu.iChatfriendService.addChatfriend(chatfriend);

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return messageId;
	}

	/**
	 * 保存离线消息
	 * 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
	public  void saveOfflineMessage(Map<String, String> map) {
		try {

			// 发送的消息
			Chatmessage chatmessage = new Chatmessage();
			chatmessage.setUserid(map.get("UI"));
			chatmessage.setFriendid(map.get("FI"));
			chatmessage.setChattype("0");// 发送0--接收1
			chatmessage.setAddtime(new Date());
			chatmessage.setIsgroup("0");// 个人0--分组1
			chatmessage.setContenttype(map.get("TP"));// 0-文本，1-图片，2-语音，3-视频，4-文件
			chatmessage.setContent(map.get("CT"));
			chatmessage.setReadstatus("0");// 是否已读 0-yes 1-no
			chatmessage.setSendstatus("0");// 是否发送成功 0-yes 1-no
			chatmessage.setFlag(map.get("T"));// 是否推送 1-yes 0-no
			mu.iChatmessageService.addChatmessage(chatmessage);

			// 对方接收的消息
			chatmessage.setUserid(map.get("FI"));
			chatmessage.setFriendid(map.get("UI"));
			chatmessage.setChattype("1");// 发送0--接收1
			chatmessage.setReadstatus("1");// 是否已读 0-yes 1-no
			chatmessage.setSendstatus("1");// 是否发送成功 0-yes 1-no
			mu.iChatmessageService.addChatmessage(chatmessage);

			Map param = new HashMap();
			param.put("fromPage", 0);
			param.put("toPage", 1);
			param.put("friendid", map.get("FI"));
			param.put("userid", map.get("UI"));
			if (mu.iChatfriendService.selectChatfriendByParam(param).size() > 0) {

			} else {
				Chatfriend chatfriend = new Chatfriend();
				chatfriend.setUserid(map.get("UI"));
				chatfriend.setIsgroup("1");
				chatfriend.setFriendid(map.get("FI"));
				chatfriend.setIsonline("1");
				mu.iChatfriendService.addChatfriend(chatfriend);

				chatfriend.setUserid(map.get("FI"));
				chatfriend.setFriendid(map.get("UI"));
				chatfriend.setIsonline("0");
				mu.iChatfriendService.addChatfriend(chatfriend);

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	/**
	 * 向除了自己其他所有人发送消息
	 * 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public void sendExceptMessage(ChannelHandlerContext channelHandlerContext,
			String type, String userid, String username) {
		// String userid=NettyChannelMap.getkey(channelHandlerContext);
		for (Map.Entry entry : NettyChannelMap.map.entrySet()) {
			if (entry.getValue() == channelHandlerContext) {

			} else {// 通知其他用户自己上线或下线
				try {
					ChannelHandlerContext temp = (ChannelHandlerContext) entry
							.getValue();
					Map<String, String> paramMap = new HashMap<String, String>();
					paramMap.put("T", type);
					paramMap.put("FI", userid);
					paramMap.put("FN", username);
					ObjectMapper mapper = new ObjectMapper();
					String json = "";
					json = mapper.writeValueAsString(paramMap);
					String content = json;
					/*
					 * ByteBuf buf = temp.alloc().buffer( 5 +
					 * content.getBytes().length); buf.writeInt(5 +
					 * content.getBytes().length);
					 * buf.writeByte(CustomHeartbeatHandler.CUSTOM_MSG);
					 * buf.writeBytes(content.getBytes());
					 * temp.writeAndFlush(buf);
					 */ 
					if(type.equals("socket")){
						SendUtils.pushMessage(temp,content);
					}
					else if(type.equals("websocket")){
						SendUtils.pushMessage(temp,new TextWebSocketFrame(content));
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			}
		}

	}

}
