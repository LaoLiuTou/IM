package com.im.service.chataddress;
import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.im.dao.chataddress.IChataddressMapper;
import com.im.model.chataddress.Chataddress;
public class ChataddressServiceImpl  implements IChataddressService {

	@Autowired
	private IChataddressMapper iChataddressMapper;
	/**
	* 通过id选取
	* @return
	*/
	public Chataddress selectChataddressById(String id){
		return iChataddressMapper.selectchataddressById(id);
	}

	/**
	* 通过查询参数获取信息
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public List<Chataddress> selectChataddressByParam(Map paramMap){ 
		return iChataddressMapper.selectchataddressByParam(paramMap);
	}

	/**
	* 通过查询参数获取总条数
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public int selectCountChataddressByParam(Map paramMap){ 
		return iChataddressMapper.selectCountchataddressByParam(paramMap);
	}

	/**
	* 更新 
	* @return 
	*/ 
	@Transactional
	public  int updateChataddress(Chataddress chataddress){
		return iChataddressMapper.updatechataddress(chataddress);
	}
	@Transactional
	public  int updateChatByUserid(Chataddress chataddress){
		return iChataddressMapper.updateChatByUserid(chataddress);
	}

	/**
	* 添加 
	* @return
	*/ 
	@Transactional
	public  int addChataddress(Chataddress chataddress){
		return iChataddressMapper.addchataddress(chataddress);
	}

	/**
	* 批量添加 
	* @return
	*/ 
	@Transactional
	public  int muladdChataddress(List<Chataddress> list){
		return iChataddressMapper.muladdchataddress(list);
	}

	/**
	* 删除 
	* @return 
	*/ 
	@Transactional
	public  int deleteChataddress(String id){
		return iChataddressMapper.deletechataddress(id);
	}

}

