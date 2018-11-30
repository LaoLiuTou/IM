package com.im.service.messagebackup;
import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.im.dao.messagebackup.IMessagebackupMapper;
import com.im.model.messagebackup.Messagebackup;
public class MessagebackupServiceImpl  implements IMessagebackupService {

	@Autowired
	private IMessagebackupMapper iMessagebackupMapper;
	/**
	* 通过id选取
	* @return
	*/
	public Messagebackup selectMessagebackupById(String id){
		return iMessagebackupMapper.selectmessagebackupById(id);
	}

	/**
	* 通过查询参数获取信息
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public List<Messagebackup> selectMessagebackupByParam(Map paramMap){ 
		return iMessagebackupMapper.selectmessagebackupByParam(paramMap);
	}

	/**
	* 通过查询参数获取总条数
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public int selectCountMessagebackupByParam(Map paramMap){ 
		return iMessagebackupMapper.selectCountmessagebackupByParam(paramMap);
	}

	/**
	* 更新 
	* @return 
	*/ 
	@Transactional
	public  int updateMessagebackup(Messagebackup messagebackup){
		return iMessagebackupMapper.updatemessagebackup(messagebackup);
	}

	/**
	* 添加 
	* @return
	*/ 
	@Transactional
	public  int addMessagebackup(Messagebackup messagebackup){
		return iMessagebackupMapper.addmessagebackup(messagebackup);
	}

	/**
	* 批量添加 
	* @return
	*/ 
	@Transactional
	public  int muladdMessagebackup(List<Messagebackup> list){
		return iMessagebackupMapper.muladdmessagebackup(list);
	}

	/**
	* 删除 
	* @return 
	*/ 
	@Transactional
	public  int deleteMessagebackup(String id){
		return iMessagebackupMapper.deletemessagebackup(id);
	}

}

