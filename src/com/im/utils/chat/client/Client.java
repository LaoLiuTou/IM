package com.im.utils.chat.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author lt
 * @version 1.0 
 */
public class Client {
    private NioEventLoopGroup workGroup = new NioEventLoopGroup(4);
    private Channel channel;
    private Bootstrap bootstrap;

    public static void main(String[] args) throws Exception {
    	/*ServiceDiscovery sd = new ServiceDiscovery("192.168.1.157:2181");
    	  String serverIp = sd.discover();
    	  String[] serverArr = serverIp.split(":");
    	  System.out.println("ServerIP:"+serverArr[0]+"    ServerPort:"+serverArr[1]);
    	  String hostIP = "192.168.1.144";
    	  int port = 8888;*/
    	  //Client.connect(hostIP, port);
    	 // Client.connect(serverArr[0], Integer.valueOf(serverArr[1]));
    	  
    	  
        Client client = new Client();
        client.start();
        
        
        
     
    }

    public void login() throws Exception {
    	if (channel != null && channel.isActive()) {
    		Map<String,String> paramMap=new HashMap<String,String>();
    		paramMap.put("T", "1");
    		paramMap.put("UI", "001");
    		paramMap.put("UN", "张四1");
    		paramMap.put("UH", "");
    		 
    		ObjectMapper mapper = new ObjectMapper();
			String json = "";
			json = mapper.writeValueAsString(paramMap);
			//System.out.println(json);
			//json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(paramMap);
			// pretty print
            String content = json;
            /*ByteBuf buf = channel.alloc().buffer(5 + content.getBytes().length);
            buf.writeInt(5 + content.getBytes().length);
            buf.writeByte(CustomHeartbeatHandler.CUSTOM_MSG.getBytes());
            buf.writeBytes(content.getBytes());
            channel.writeAndFlush(buf);*/
            
            ByteBuf buf = Unpooled.buffer(json.getBytes().length);
    		buf.writeBytes(json.getBytes());
    		channel.writeAndFlush(buf);
    		
            System.out.println(content);
           // channel.writeAndFlush(content);
        }
    }
    
    

    
    public void start() {
        try {
            bootstrap = new Bootstrap();
            bootstrap
                    .group(workGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline p = socketChannel.pipeline();
                            p.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
                            p.addLast("decoder", new StringDecoder());
                            p.addLast("encoder", new StringEncoder());
                            p.addLast(new IdleStateHandler(10, 0, 0));
                            p.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, -4, 0));
                            p.addLast(new ClientHandler(Client.this));
                            
                        }
                    });
            doConnect();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void doConnect() {
        if (channel != null && channel.isActive()) {
            return;
        }

        ChannelFuture future = bootstrap.connect("127.0.0.1", 8888);
        
        future.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture futureListener) throws Exception {
                if (futureListener.isSuccess()) {
                    channel = futureListener.channel();
                    System.out.println("Connect to server successfully!");
                   Thread.sleep(1000);
                    //登录用户
                    login();
                    
                } else {
                    System.out.println("Failed to connect to server, try connect after 10s");

                    futureListener.channel().eventLoop().schedule(new Runnable() {
                        public void run() {
                            doConnect();
                        }
                    }, 10, TimeUnit.SECONDS);
                }
            }
        });
    }

}



