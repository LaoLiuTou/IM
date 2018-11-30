package com.im.utils.chat.client;

public class ByteUtil {
	//将字节数组转换为short类型，即统计字符串长度  
    public static short bytes2Short2(byte[] b) {  
        short i = (short) (((b[1] & 0xff) << 8) | b[0] & 0xff);  
        return i;  
    }  
    //将字节数组转换为16进制字符串  
    public static String BinaryToHexString(byte[] bytes) {  
        String hexStr = "0123456789ABCDEF";  
        String result = "";  
        String hex = "";  
        for (byte b : bytes) {  
            hex = String.valueOf(hexStr.charAt((b & 0xF0) >> 4));  
            hex += String.valueOf(hexStr.charAt(b & 0x0F));  
            result += hex + " ";  
        }  
        return result;  
    }  
}
