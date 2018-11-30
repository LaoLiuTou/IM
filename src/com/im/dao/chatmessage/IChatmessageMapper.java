package com.im.dao.chatmessage;
import java.util.List;
import java.util.Map;
import com.im.model.chatmessage.Chatmessage;
	public interface IChatmessageMapper {
	/**
 	* 通过id选取
 	* @return
 	*/
	public Chatmessage selectchatmessageById(String id);
	/**
 	* 通过查询参数获取信息
 	* @return
 */ 
 @SuppressWarnings("rawtypes")
	public List<Chatmessage> selectchatmessageByParam(Map paramMap); 
	/**
		* 通过查询参数获取总条数
	    * @return
	*/ 
	@SuppressWarnings("rawtypes")
	public int selectCountchatmessageByParam(Map paramMap); 
	/**
 	* 更新 
 	* @return 
 */ 
	public  int updatechatmessage(Chatmessage chatmessage);
	/**
 	* 添加 
 	* @return
 */ 
	public  int addchatmessage(Chatmessage chatmessage);
	/**
 	* 批量添加 
 	* @return
 */ 
	public  int muladdchatmessage(List<Chatmessage> list);
	/**
 	* 删除 
 	* @return 
 */ 
public  int deletechatmessage(String id);

}

