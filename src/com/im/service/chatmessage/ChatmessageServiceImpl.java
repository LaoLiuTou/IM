package com.im.service.chatmessage;
import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.im.dao.chatmessage.IChatmessageMapper;
import com.im.model.chatmessage.Chatmessage;
public class ChatmessageServiceImpl  implements IChatmessageService {

	@Autowired
	private IChatmessageMapper iChatmessageMapper;
	/**
	* 通过id选取
	* @return
	*/
	public Chatmessage selectChatmessageById(String id){
		return iChatmessageMapper.selectchatmessageById(id);
	}

	/**
	* 通过查询参数获取信息
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public List<Chatmessage> selectChatmessageByParam(Map paramMap){ 
		return iChatmessageMapper.selectchatmessageByParam(paramMap);
	}

	/**
	* 通过查询参数获取总条数
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public int selectCountChatmessageByParam(Map paramMap){ 
		return iChatmessageMapper.selectCountchatmessageByParam(paramMap);
	}

	/**
	* 更新 
	* @return 
	*/ 
	@Transactional
	public  int updateChatmessage(Chatmessage chatmessage){
		return iChatmessageMapper.updatechatmessage(chatmessage);
	}

	/**
	* 添加 
	* @return
	*/ 
	@Transactional
	public  int addChatmessage(Chatmessage chatmessage){
		return iChatmessageMapper.addchatmessage(chatmessage);
	}

	/**
	* 批量添加 
	* @return
	*/ 
	@Transactional
	public  int muladdChatmessage(List<Chatmessage> list){
		return iChatmessageMapper.muladdchatmessage(list);
	}

	/**
	* 删除 
	* @return 
	*/ 
	@Transactional
	public  int deleteChatmessage(String id){
		return iChatmessageMapper.deletechatmessage(id);
	}

}

