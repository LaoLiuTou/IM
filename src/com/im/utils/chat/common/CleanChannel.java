package com.im.utils.chat.common;

import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
 
@Component 
public class CleanChannel {
	 
	Logger logger = Logger.getLogger("IMLogger");
	
	private long cleanTimes=5*60*1000;
	 
	@Scheduled(cron="0 0/5 * * * ? ")  
	public void cleanChannel(){  
		long currentTime=new Date().getTime();
		for (Map.Entry<String,Long> entry:NettyChannelMap.timemap.entrySet()){ 
			if((currentTime-entry.getValue())>cleanTimes){
				NettyChannelMap.removeByKey(entry.getKey()); 
				logger.info("移除："+entry.getKey());
			}
            	
        }
		
	}
}