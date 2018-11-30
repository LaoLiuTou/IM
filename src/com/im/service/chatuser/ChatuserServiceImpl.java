package com.im.service.chatuser;
import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.im.dao.chatuser.IChatuserMapper;
import com.im.model.chatuser.Chatuser;
public class ChatuserServiceImpl  implements IChatuserService {

	@Autowired
	private IChatuserMapper iChatuserMapper;
	/**
	* 通过id选取
	* @return
	*/
	public Chatuser selectChatuserById(String id){
		return iChatuserMapper.selectchatuserById(id);
	}

	/**
	* 通过查询参数获取信息
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public List<Chatuser> selectChatuserByParam(Map paramMap){ 
		return iChatuserMapper.selectchatuserByParam(paramMap);
	}

	/**
	* 通过查询参数获取总条数
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public int selectCountChatuserByParam(Map paramMap){ 
		return iChatuserMapper.selectCountchatuserByParam(paramMap);
	}

	/**
	* 更新 
	* @return 
	*/ 
	@Transactional
	public  int updateChatuser(Chatuser chatuser){
		return iChatuserMapper.updatechatuser(chatuser);
	}

	/**
	* 添加 
	* @return
	*/ 
	@Transactional
	public  int addChatuser(Chatuser chatuser){
		return iChatuserMapper.addchatuser(chatuser);
	}

	/**
	* 批量添加 
	* @return
	*/ 
	@Transactional
	public  int muladdChatuser(List<Chatuser> list){
		return iChatuserMapper.muladdchatuser(list);
	}

	/**
	* 删除 
	* @return 
	*/ 
	@Transactional
	public  int deleteChatuser(String id){
		return iChatuserMapper.deletechatuser(id);
	}

}

