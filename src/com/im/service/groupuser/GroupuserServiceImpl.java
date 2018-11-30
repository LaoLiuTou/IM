package com.im.service.groupuser;
import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.im.dao.groupuser.IGroupuserMapper;
import com.im.model.groupuser.Groupuser;
public class GroupuserServiceImpl  implements IGroupuserService {

	@Autowired
	private IGroupuserMapper iGroupuserMapper;
	/**
	* 通过id选取
	* @return
	*/
	public Groupuser selectGroupuserById(String id){
		return iGroupuserMapper.selectgroupuserById(id);
	}

	/**
	* 通过查询参数获取信息
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public List<Groupuser> selectGroupuserByParam(Map paramMap){ 
		return iGroupuserMapper.selectgroupuserByParam(paramMap);
	}

	/**
	* 通过查询参数获取总条数
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public int selectCountGroupuserByParam(Map paramMap){ 
		return iGroupuserMapper.selectCountgroupuserByParam(paramMap);
	}

	/**
	* 更新 
	* @return 
	*/ 
	@Transactional
	public  int updateGroupuser(Groupuser groupuser){
		return iGroupuserMapper.updategroupuser(groupuser);
	}

	/**
	* 添加 
	* @return
	*/ 
	@Transactional
	public  int addGroupuser(Groupuser groupuser){
		return iGroupuserMapper.addgroupuser(groupuser);
	}

	/**
	* 批量添加 
	* @return
	*/ 
	@Transactional
	public  int muladdGroupuser(List<Groupuser> list){
		return iGroupuserMapper.muladdgroupuser(list);
	}

	/**
	* 删除 
	* @return 
	*/ 
	@Transactional
	public  int deleteGroupuser(String id){
		return iGroupuserMapper.deletegroupuser(id);
	}

}

