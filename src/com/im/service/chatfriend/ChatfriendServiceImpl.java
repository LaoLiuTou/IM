package com.im.service.chatfriend;
import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.im.dao.chatfriend.IChatfriendMapper;
import com.im.model.chatfriend.Chatfriend;
public class ChatfriendServiceImpl  implements IChatfriendService {

	@Autowired
	private IChatfriendMapper iChatfriendMapper;
	/**
	* 通过id选取
	* @return
	*/
	public Chatfriend selectChatfriendById(String id){
		return iChatfriendMapper.selectchatfriendById(id);
	}

	/**
	* 通过查询参数获取信息
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public List<Chatfriend> selectChatfriendByParam(Map paramMap){ 
		return iChatfriendMapper.selectchatfriendByParam(paramMap);
	}

	/**
	* 通过查询参数获取总条数
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public int selectCountChatfriendByParam(Map paramMap){ 
		return iChatfriendMapper.selectCountchatfriendByParam(paramMap);
	}

	/**
	* 更新 
	* @return 
	*/ 
	@Transactional
	public  int updateChatfriend(Chatfriend chatfriend){
		return iChatfriendMapper.updatechatfriend(chatfriend);
	}

	/**
	* 添加 
	* @return
	*/ 
	@Transactional
	public  int addChatfriend(Chatfriend chatfriend){
		return iChatfriendMapper.addchatfriend(chatfriend);
	}

	/**
	* 批量添加 
	* @return
	*/ 
	@Transactional
	public  int muladdChatfriend(List<Chatfriend> list){
		return iChatfriendMapper.muladdchatfriend(list);
	}

	/**
	* 删除 
	* @return 
	*/ 
	@Transactional
	public  int deleteChatfriend(String id){
		return iChatfriendMapper.deletechatfriend(id);
	}

}

