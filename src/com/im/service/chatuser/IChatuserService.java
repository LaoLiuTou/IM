package com.im.service.chatuser;
import java.util.List;
import java.util.Map;
import com.im.model.chatuser.Chatuser;
public interface IChatuserService {
	/**
	* 通过id选取
	* @return
	*/
	public Chatuser selectChatuserById(String id);

	/**
	* 通过查询参数获取信息
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public List<Chatuser> selectChatuserByParam(Map paramMap); 

	/**
	* 通过查询参数获取总条数
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public int selectCountChatuserByParam(Map paramMap); 

	/**
	* 更新 
	* @return 
	*/ 
	public int updateChatuser(Chatuser chatuser);

	/**
	* 添加 
	* @return
	*/ 
	public int addChatuser(Chatuser chatuser);

	/**
	* 批量添加 
	* @return
	*/ 
	public int muladdChatuser(List<Chatuser> list);

	/**
	* 删除 
	* @return 
	*/ 
	public int deleteChatuser(String id);

}

