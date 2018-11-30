package com.im.service.messagebackup;
import java.util.List;
import java.util.Map;
import com.im.model.messagebackup.Messagebackup;
public interface IMessagebackupService {
	/**
	* 通过id选取
	* @return
	*/
	public Messagebackup selectMessagebackupById(String id);

	/**
	* 通过查询参数获取信息
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public List<Messagebackup> selectMessagebackupByParam(Map paramMap); 

	/**
	* 通过查询参数获取总条数
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public int selectCountMessagebackupByParam(Map paramMap); 

	/**
	* 更新 
	* @return 
	*/ 
	public int updateMessagebackup(Messagebackup messagebackup);

	/**
	* 添加 
	* @return
	*/ 
	public int addMessagebackup(Messagebackup messagebackup);

	/**
	* 批量添加 
	* @return
	*/ 
	public int muladdMessagebackup(List<Messagebackup> list);

	/**
	* 删除 
	* @return 
	*/ 
	public int deleteMessagebackup(String id);

}

