package com.im.service.chataddress;
import java.util.List;
import java.util.Map;
import com.im.model.chataddress.Chataddress;
public interface IChataddressService {
	/**
	* 通过id选取
	* @return
	*/
	public Chataddress selectChataddressById(String id);

	/**
	* 通过查询参数获取信息
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public List<Chataddress> selectChataddressByParam(Map paramMap); 

	/**
	* 通过查询参数获取总条数
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public int selectCountChataddressByParam(Map paramMap); 

	/**
	* 更新 
	* @return 
	*/ 
	public int updateChataddress(Chataddress chataddress);
	public int updateChatByUserid(Chataddress chataddress);

	/**
	* 添加 
	* @return
	*/ 
	public int addChataddress(Chataddress chataddress);

	/**
	* 批量添加 
	* @return
	*/ 
	public int muladdChataddress(List<Chataddress> list);

	/**
	* 删除 
	* @return 
	*/ 
	public int deleteChataddress(String id);

}

