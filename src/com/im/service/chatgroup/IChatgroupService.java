package com.im.service.chatgroup;
import java.util.List;
import java.util.Map;
import com.im.model.chatgroup.Chatgroup;
public interface IChatgroupService {
	/**
	* 通过id选取
	* @return
	*/
	public Chatgroup selectChatgroupById(String id);

	/**
	* 通过查询参数获取信息
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public List<Chatgroup> selectChatgroupByParam(Map paramMap); 

	/**
	* 通过查询参数获取总条数
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public int selectCountChatgroupByParam(Map paramMap); 

	/**
	* 更新 
	* @return 
	*/ 
	public int updateChatgroup(Chatgroup chatgroup);

	/**
	* 添加 
	* @return
	*/ 
	public int addChatgroup(Chatgroup chatgroup);

	/**
	* 批量添加 
	* @return
	*/ 
	public int muladdChatgroup(List<Chatgroup> list);

	/**
	* 删除 
	* @return 
	*/ 
	public int deleteChatgroup(String id);

}

