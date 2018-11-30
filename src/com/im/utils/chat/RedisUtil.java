package com.im.utils.chat;

import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
 

 
import redis.clients.jedis.Jedis;  

import redis.clients.jedis.JedisPool;  
  
import redis.clients.jedis.JedisPoolConfig;  
  
public class RedisUtil {
	
	
	  private static JedisPool pool;      
	  private static String tag="IM:";      
	  
	    //静态代码初始化池配置    
	  
	    static {      
	  
	      try{  
	  
	    	 Properties props = new Properties();  
	      	 props.load(RedisUtil.class.getClassLoader().getResourceAsStream("redis/redis.properties"));  
	     	 //创建jedis池配置实例    
             JedisPoolConfig config = new JedisPoolConfig();     
             //设置池配置项值    
             config.setMaxActive(Integer.valueOf(props.getProperty("jedis.pool.maxActive")));      
             config.setMaxIdle(Integer.valueOf(props.getProperty("jedis.pool.maxIdle")));      
             config.setMaxWait(Long.valueOf(props.getProperty("jedis.pool.maxWait")));      
             config.setTestOnBorrow(Boolean.valueOf(props.getProperty("jedis.pool.testOnBorrow")));      
             config.setTestOnReturn(Boolean.valueOf(props.getProperty("jedis.pool.testOnReturn")));     
             //根据配置实例化jedis池    
             pool = new JedisPool(config, props.getProperty("redis.ip"), Integer.valueOf(props.getProperty("redis.port")));   
	      }catch (Exception e) {  
			e.printStackTrace();  
		 }  
			  
	    }    
	  
	      
	  
	    /**获得jedis对象*/  
	  
	    public static Jedis getJedisObject(){  
	  
	     return pool.getResource();  
	  
	    }  
	  
	      
	  
	    /**归还jedis对象*/  
	  
	    public static void recycleJedisOjbect(Jedis jedis){  
	  
	     pool.returnResource(jedis);    
	  
	    }  
	  
	    
	    public static String getObject(String id) {
	    	Jedis jedis = getJedisObject();//获得jedis实例 
	        String info = jedis.get(tag+id);
	        recycleJedisOjbect(jedis); //将 获取的jedis实例对象还回迟中  
	        return info;

	   }
	    public static void getList() {
	    	Jedis jedis = getJedisObject();//获得jedis实例 
	    	 Set<String> keys = jedis.keys(tag+"*"); 
	         Iterator<String> it=keys.iterator() ;   
	         while(it.hasNext()){   
	             String key = it.next();   
	             System.out.println(key +":"+jedis.get(key));   
	         }
	    	//byte[] chc = jedis.get((id).getBytes());
	    	//recycleJedisOjbect(jedis); //将 获取的jedis实例对象还回迟中  
	    	//return (CHC) SerializeUtil.unserialize(chc);
	    	
	    }
	    
	    public static void setObject(String id,String info) {
	    	Jedis jedis = getJedisObject();//获得jedis实例 
	        try {
				jedis.set(tag+id, info);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        recycleJedisOjbect(jedis); //将 获取的jedis实例对象还回迟中  
	    }
	    
	    public static void delOject(String key){
	    	Jedis jedis = getJedisObject();//获得jedis实例 
	        boolean isExist=jedis.exists(tag+key);
	        if(isExist){
	            System.out.println("delete the key:"+key);
	            jedis.del(tag+key);
	        }
	        recycleJedisOjbect(jedis); //将 获取的jedis实例对象还回迟中  
	    }      
	     
	    /**  
	 
	     * 测试jedis池方法  
	 
	     */    
	  
	    public static void main(String[] args) {  
	  
	        Jedis jedis = getJedisObject();//获得jedis实例                    
	  
	        //获取jedis实例后可以对redis服务进行一系列的操作    
	  
	        /*jedis.set("name", "zhuxun");    
	  
	        System.out.println(jedis.get("name"));    
	  
	        jedis.del("name");    
	  
	        System.out.println(jedis.exists("name"));    */
	        //{"T":"1","UN":"李三","UI":"002","UH":""}
	       /*delOject("102");
	        delOject("144");*/
	          
        	getList();
	        recycleJedisOjbect(jedis); //将 获取的jedis实例对象还回迟中  
	  
	    }    
	  

}

