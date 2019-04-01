package com.im.controller.push;
import io.netty.channel.ChannelHandlerContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.im.model.chatuser.Chatuser;
import com.im.service.chatuser.IChatuserService;
import com.im.utils.chat.common.NettyChannelMap;
import com.im.utils.chat.server.MessageUtil;
@Controller
public class PushController { 
	@Autowired
	private IChatuserService iChatuserService;  
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Logger logger = Logger.getLogger("IMLogger");
	private String userid;
	private String friendid;//拆分
	private String content;
	private String delay;
	private String username;
	private String type;
	private String flag;
	private String headimage;
	private String key;
	private String operate;
	private Timer timer;
    private static Map<String,Timer> timerMap = new HashMap<String, Timer>();
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/PushServer")
	@ResponseBody
	public Map push(HttpServletRequest request){
		userid=request.getParameter("userid");
		friendid=request.getParameter("friendid");
		content=request.getParameter("content");
		delay=request.getParameter("delay");
		username=request.getParameter("username");
		type=request.getParameter("type");
		flag=request.getParameter("flag");
		headimage=request.getParameter("headimage");
		key=request.getParameter("key");
		operate=request.getParameter("operate");
		String message="";
		Map resultMap=new HashMap();
		try {
			if(content!=null&&content.length()>0){
				//消息id
				if(key!=null&&key.length()>0){
					if(operate!=null&&operate.equals("0")){
						message="新建消息队列。";
						//////新建定时任务
						timer = new Timer(true); 
						timerMap.put(key, timer);
						if(delay!=null&&!delay.equals("")&&Long.parseLong(delay)>0){
							timer.schedule(new SendTask(key), Long.parseLong(delay));  
						}
						else{
							timer.schedule(new SendTask(key), 1000);  
						}
					}
					else if(operate!=null&&operate.equals("1")){
						Timer tempTimer=timerMap.get(key);
						if(tempTimer!=null){
							tempTimer.cancel();
							timerMap.remove(key);
						}
						message="修改消息队列。";
						//////新建定时任务
						timer = new Timer(true); 
						timerMap.put(key, timer);
						if(delay!=null&&!delay.equals("")&&Long.parseLong(delay)>0){
							timer.schedule(new SendTask(key), Long.parseLong(delay));  
						}
						else{
							timer.schedule(new SendTask(key), 1000);  
						}
						logger.info("修改消息：KEY:"+key);
					}
					else if(operate!=null&&operate.equals("2")){
						Timer tempTimer=timerMap.get(key);
						if(tempTimer!=null){
							tempTimer.cancel();
							timerMap.remove(key);
						}
						message="取消消息队列。";
						logger.info("取消消息：KEY:"+key);
					}
					else{
						message="未操作消息队列。";
					}
					
					resultMap.put("state", "success");
					 
				}
				else{
					resultMap.put("state", "failure"); 
					message="消息ID为空！";
					logger.info("消息ID为空！");
				}
			}
			else{
				resultMap.put("state", "failure");  
				message="消息内容为空！";
				logger.info("消息内容为空！");
			}
			
			resultMap.put("status", "0");
			resultMap.put("msg", message);
			logger.info("收到推送消息，KEY:"+key+"发送者："+userid+";接收者："+friendid+";"+"延迟发送时间："+delay+";操作类型："+operate);
			
		} catch (Exception e) {
			resultMap.put("state", "failure");  
			resultMap.put("msg", message);
			logger.info("新建失败！"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
	 
	class SendTask extends TimerTask {  
		  
		private String taskKey;
	    public SendTask(String key) {
			// TODO Auto-generated constructor stub
	    	this.taskKey=key;
		}
		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override  
	    public void run() {  
	        try {
	        	logger.info("taskKey:"+taskKey);
	        	//保存用户信息
	        	Chatuser chatuser= new Chatuser();
				chatuser.setUserid(userid);
				chatuser.setUsername(username);
				chatuser.setUserimage(headimage);
				chatuser.setAddtime(new Date());
				chatuser.setIsonline("0");
				Map  param = new HashMap ();
				param.put("fromPage",0);
				param.put("toPage",100); 
				param.put("userid", userid);
				List<Chatuser> tempList = iChatuserService.selectChatuserByParam(param);
				if(tempList.size()==0){
					iChatuserService.addChatuser(chatuser);
				}
				else{
					iChatuserService.updateChatuser(chatuser);
				}
				Properties props = new Properties();  
	            props.load(PushController.class.getClassLoader().getResourceAsStream("socket/socket.properties"));
	            String nettype=props.getProperty("nettype").trim();
	        	String[] friendids = friendid.split("\\|",-1);
				for(String friend:friendids){
					if(friend.length()>0){
						ChannelHandlerContext channelHandlerContext=NettyChannelMap.map.get(friend);
						
						Map<String, String> paramMap = new HashMap<String, String>();
						paramMap.put("T", "7");
						paramMap.put("UI", userid);
						paramMap.put("UN", username);
						paramMap.put("FI", friend); 
						//paramMap.put("CT", Base64.getBase64(content));
						paramMap.put("CT", content);
						paramMap.put("TP", type);
						paramMap.put("PT", flag);
						paramMap.put("HI", headimage);
						new MessageUtil().sendOnlineMessage(channelHandlerContext,paramMap,nettype);
					}
				}
				timerMap.remove(taskKey);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
	    }  
	}  
}
