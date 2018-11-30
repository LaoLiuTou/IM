package com.im.service.chatmessage;
import java.util.List;
import java.util.Map;
import com.im.model.chatmessage.Chatmessage;
public interface IChatmessageService {
	/**
	* 通过id选取
	* @return
	*/
	public Chatmessage selectChatmessageById(String id);

	/**
	* 通过查询参数获取信息
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public List<Chatmessage> selectChatmessageByParam(Map paramMap); 

	/**
	* 通过查询参数获取总条数
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public int selectCountChatmessageByParam(Map paramMap); 

	/**
	* 更新 
	* @return 
	*/ 
	public int updateChatmessage(Chatmessage chatmessage);

	/**
	* 添加 
	* @return
	*/ 
	public int addChatmessage(Chatmessage chatmessage);

	/**
	* 批量添加 
	* @return
	*/ 
	public int muladdChatmessage(List<Chatmessage> list);

	/**
	* 删除 
	* @return 
	*/ 
	public int deleteChatmessage(String id);

}

