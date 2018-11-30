package com.im.utils.chat;

import java.io.FileInputStream;
import java.util.Properties;

import javapns.notification.AppleNotificationServerBasicImpl;
import javapns.notification.PushNotificationManager;

public class ApnsTools {
	
	
	public static PushNotificationManager pushManager;
	
	
	public ApnsTools(){
		
		if(pushManager!=null){
			
		}else{
			
			try {
				//ios push
				String base = getClass().getResource("/")
						.getPath()+"iosKeys/";
				//参数
		   	 	Properties properties = new Properties();
				 
				try {
					properties.load(new FileInputStream(base
							+ "ioskey.properties"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}   
				String keyName=properties.getProperty("keyName").trim();
				String password=properties.getProperty("password").trim();
				String status=properties.getProperty("status").trim();
				String path=base+keyName;
				pushManager = new PushNotificationManager();
				//true：表示的是产品发布推送服务 false：表示的是产品测试推送服务
				if(status.equals("true")){
					pushManager.initializeConnection(new AppleNotificationServerBasicImpl(path, password, true));
				}
				else{
					pushManager.initializeConnection(new AppleNotificationServerBasicImpl(path, password, false));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e.toString());
				e.printStackTrace();
			} 
			
		}
	
		
	}

}
