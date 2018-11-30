package com.im.service.groupuser;
import java.util.List;
import java.util.Map;
import com.im.model.groupuser.Groupuser;
public interface IGroupuserService {
	/**
	* 通过id选取
	* @return
	*/
	public Groupuser selectGroupuserById(String id);

	/**
	* 通过查询参数获取信息
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public List<Groupuser> selectGroupuserByParam(Map paramMap); 

	/**
	* 通过查询参数获取总条数
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public int selectCountGroupuserByParam(Map paramMap); 

	/**
	* 更新 
	* @return 
	*/ 
	public int updateGroupuser(Groupuser groupuser);

	/**
	* 添加 
	* @return
	*/ 
	public int addGroupuser(Groupuser groupuser);

	/**
	* 批量添加 
	* @return
	*/ 
	public int muladdGroupuser(List<Groupuser> list);

	/**
	* 删除 
	* @return 
	*/ 
	public int deleteGroupuser(String id);

}

