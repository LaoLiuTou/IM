package com.im.service.chatfriend;
import java.util.List;
import java.util.Map;
import com.im.model.chatfriend.Chatfriend;
public interface IChatfriendService {
	/**
	* 通过id选取
	* @return
	*/
	public Chatfriend selectChatfriendById(String id);

	/**
	* 通过查询参数获取信息
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public List<Chatfriend> selectChatfriendByParam(Map paramMap); 

	/**
	* 通过查询参数获取总条数
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public int selectCountChatfriendByParam(Map paramMap); 

	/**
	* 更新 
	* @return 
	*/ 
	public int updateChatfriend(Chatfriend chatfriend);

	/**
	* 添加 
	* @return
	*/ 
	public int addChatfriend(Chatfriend chatfriend);

	/**
	* 批量添加 
	* @return
	*/ 
	public int muladdChatfriend(List<Chatfriend> list);

	/**
	* 删除 
	* @return 
	*/ 
	public int deleteChatfriend(String id);

}

