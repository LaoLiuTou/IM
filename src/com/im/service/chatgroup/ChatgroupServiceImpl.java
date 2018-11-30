package com.im.service.chatgroup;
import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.im.dao.chatgroup.IChatgroupMapper;
import com.im.model.chatgroup.Chatgroup;
public class ChatgroupServiceImpl  implements IChatgroupService {

	@Autowired
	private IChatgroupMapper iChatgroupMapper;
	/**
	* 通过id选取
	* @return
	*/
	public Chatgroup selectChatgroupById(String id){
		return iChatgroupMapper.selectchatgroupById(id);
	}

	/**
	* 通过查询参数获取信息
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public List<Chatgroup> selectChatgroupByParam(Map paramMap){ 
		return iChatgroupMapper.selectchatgroupByParam(paramMap);
	}

	/**
	* 通过查询参数获取总条数
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public int selectCountChatgroupByParam(Map paramMap){ 
		return iChatgroupMapper.selectCountchatgroupByParam(paramMap);
	}

	/**
	* 更新 
	* @return 
	*/ 
	@Transactional
	public  int updateChatgroup(Chatgroup chatgroup){
		return iChatgroupMapper.updatechatgroup(chatgroup);
	}

	/**
	* 添加 
	* @return
	*/ 
	@Transactional
	public  int addChatgroup(Chatgroup chatgroup){
		return iChatgroupMapper.addchatgroup(chatgroup);
	}

	/**
	* 批量添加 
	* @return
	*/ 
	@Transactional
	public  int muladdChatgroup(List<Chatgroup> list){
		return iChatgroupMapper.muladdchatgroup(list);
	}

	/**
	* 删除 
	* @return 
	*/ 
	@Transactional
	public  int deleteChatgroup(String id){
		return iChatgroupMapper.deletechatgroup(id);
	}

}

