package com.im.utils.chat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.im.utils.chat.server.SocketServerInitializer;
import com.im.utils.chat.server.WebsocketServerInitializer;
@Component
public class ServerManager {  
	Logger logger = Logger.getLogger("IMLogger");
	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;
	
    //本服务socket地址
    public static String socketIp; 
    public static String socketPort; 
    public static String nettype; 
    public static String cacheType; 
    
	 
    @PostConstruct  
    public void startServer(){
    	threadPoolTaskExecutor.execute(new Runnable() {
		  public void run() {
			  try {
	            Properties props = new Properties();  
	            props.load(ServerManager.class.getClassLoader().getResourceAsStream("socket/socket.properties"));
	            socketIp=props.getProperty("ip").trim();
	            socketPort=props.getProperty("port").trim();
	            nettype=props.getProperty("nettype").trim();
	            cacheType=props.getProperty("cachetype").trim();
	            
	            //启动服务
	            nettyServer();
		  
		       } catch (IOException e) {
		 			// TODO Auto-generated catch block
		 			e.printStackTrace();
		 	   }
		  }
	   	},5000);
    	
    }
   
    public void nettyServer() {
		EventLoopGroup bossGroup = new NioEventLoopGroup(); 
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap(); 
            if(nettype.equals("socket")){
           	 	b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class) 
                .childHandler(new SocketServerInitializer()) 
                .option(ChannelOption.SO_BACKLOG, 128)         
                .childOption(ChannelOption.SO_KEEPALIVE, true);
            }
            else if(nettype.equals("websocket")){
             
           	 	b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new WebsocketServerInitializer()) 
                .option(ChannelOption.SO_BACKLOG, 128)         
                .childOption(ChannelOption.SO_KEEPALIVE, true); 
            }
            if(cacheType==null||cacheType.equals("")){
            	 logger.info("用户同步方式:"+"单服务不需要同步！");
            }
            else{
            	 logger.info("用户同步方式:"+cacheType);
            }
            logger.info("服务类型："+nettype);
            logger.info("启动IMServer");
            
    		
            int port=Integer.parseInt(socketPort);
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        }
        catch (Exception e) {
			// TODO: handle exception
       	 	e.printStackTrace();
		}
        finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            logger.info("关闭IMServer");
        }
    }
	
	public static void main(String[] args){
		
	}
} 