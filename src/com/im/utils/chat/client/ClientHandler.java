package com.im.utils.chat.client;

import io.netty.channel.ChannelHandlerContext;

import com.im.utils.chat.common.CustomHeartbeatHandler;

/**
 * @author lt
 * @version 1.0 
 */
public class ClientHandler extends CustomHeartbeatHandler {
    private Client client;
    public ClientHandler(Client client) {
        super("client");
        this.client = client;
    }

    @Override
    protected void handleData(ChannelHandlerContext channelHandlerContext, Object string) {
		try {
			System.out.println("client:"+string);
			/*byte[] data = new byte[byteBuf.readableBytes() - 5];
			byteBuf.skipBytes(5);
			byteBuf.readBytes(data);
			String content = new String(data);
			
			ObjectMapper mapper = new ObjectMapper();
			Map<String, String> map = new HashMap<String, String>(); 
			map = mapper.readValue(content, new TypeReference<Map<String, String>>(){});
			if(map.get("T").equals("1")){//login
		       
			}
			else if(map.get("T").equals("2")){//logout
				
			}
			else if(map.get("T").equals("3")){//text message
				System.out.println(map.get("FN") + ":" + map);
				
			}*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    protected void handleAllIdle(ChannelHandlerContext ctx) {
        super.handleAllIdle(ctx);
        //sendPingMsg(ctx);
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        client.doConnect();
    }

	 
}