package com.im.utils.chat.server;



import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import org.apache.log4j.Logger;

/**
 * 服务端 ChannelInitializer
 */
public class SocketServerInitializer extends
		ChannelInitializer<SocketChannel> {

	@Override
    public void initChannel(SocketChannel ch) throws Exception {
		Logger logger = Logger.getLogger("NettyChatLogger");
		 ChannelPipeline pipeline = ch.pipeline(); 
		 
		 //ByteBuf delimiter = Unpooled.copiedBuffer("&end==".getBytes());
		 //pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, delimiter));
         
 
		pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));// 这里设置：不删除分隔符，
        pipeline.addLast("decoder", new StringDecoder(Charset.forName("GBK")));
        pipeline.addLast("encoder", new StringEncoder(Charset.forName("GBK")));
        pipeline.addLast("handler", new SocketServerHandler());
        pipeline.addLast(new IdleStateHandler(10, 0, 0));
        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, -4, 0)); 
        
        /*pipeline.addLast("http-codec",new HttpServerCodec());
        pipeline.addLast("aggregator",new HttpObjectAggregator(65536));
        ch.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
        pipeline.addLast("handler",new WebSocketServerHandler());*/
 
        logger.info("客户端:"+ch.remoteAddress() +"已连接");
    }
}
