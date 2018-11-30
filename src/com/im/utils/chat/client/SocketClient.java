package com.im.utils.chat.client;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
  
public class SocketClient {  
	
	/**
	 * 转发消息   
	 */
	public static void transmitMessage(String address,String message){
		
		try {  
			Socket socket = SocketMap.get(address);
			if(socket == null ||socket.isClosed()){
				String addr = address.split(":")[0];
				String port = address.split(":")[1];
				socket= new Socket(addr, Integer.parseInt(port));  
			}
			else{
				
			}
            OutputStream os = new BufferedOutputStream(socket.getOutputStream()); 
            byte[] bi = (message+"\n").getBytes("GBK");  
            os.write(bi);  
            os.flush();  
            os.close();    
        } catch (Exception e) {  
            System.out.println(e.getMessage());  
            e.printStackTrace();  
        }  
	}
	
	
	
	
	
	
	
    public static final String IP_ADDR = "192.168.1.27";//服务器地址   
    public static final int PORT = 2000;//服务器端口号    
    public static void main(String[] args) throws Exception{       
    	try {  
            Socket s = new Socket(IP_ADDR, PORT);  
            OutputStream os = new BufferedOutputStream(s.getOutputStream());  
            DataInputStream input = new DataInputStream(s.getInputStream());    
            Map<String,String> paramMap=new HashMap<String,String>();
    		
           /* paramMap.put("T", "1");
    		paramMap.put("UI", "001");
    		paramMap.put("UN", "张四1");
    		paramMap.put("UH", "");*/
    		
    		
    		paramMap.put("T", "3");
    		paramMap.put("UI", "001");
    		paramMap.put("FI", "003");
    		paramMap.put("CT", "contentcccccc");
    		 
    		ObjectMapper mapper = new ObjectMapper();
			String json = "";
			json = mapper.writeValueAsString(paramMap)+"\n";
             
            byte[] bi = json.getBytes("GBK");  
            os.write(bi);  
            os.flush();  
             
          //{"T":"10","UI":"101","FI":"102","UN":"XX","CT":"5b6X562J5Yiw","TP":"1","HI":""}
           //{"T":"1","UN":"张1","UI":"102","UH":""}
            boolean isconnect= true;
            while (isconnect) {
           	 int rbsize = input.available();
           	 if (rbsize > 0) {
                    byte[] b = new byte[rbsize];
                    input.read(b);
                    //这里读取字符串，如果不正确
                    String strOutput= new String(b, "GBK");  
                    System.out.println(strOutput);
                }
           	 
	             
			}  
            os.close();   
            input.close();  
        } catch (IOException e) {  
            System.out.println(e.getMessage());  
            e.printStackTrace();  
        }  
    }      
}   
