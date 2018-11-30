package com.im.dao.groupuser;
import java.util.List;
import java.util.Map;
import com.im.model.groupuser.Groupuser;
	public interface IGroupuserMapper {
	/**
 	* 通过id选取
 	* @return
 	*/
	public Groupuser selectgroupuserById(String id);
	/**
 	* 通过查询参数获取信息
 	* @return
 */ 
 @SuppressWarnings("rawtypes")
	public List<Groupuser> selectgroupuserByParam(Map paramMap); 
	/**
		* 通过查询参数获取总条数
	    * @return
	*/ 
	@SuppressWarnings("rawtypes")
	public int selectCountgroupuserByParam(Map paramMap); 
	/**
 	* 更新 
 	* @return 
 */ 
	public  int updategroupuser(Groupuser groupuser);
	/**
 	* 添加 
 	* @return
 */ 
	public  int addgroupuser(Groupuser groupuser);
	/**
 	* 批量添加 
 	* @return
 */ 
	public  int muladdgroupuser(List<Groupuser> list);
	/**
 	* 删除 
 	* @return 
 */ 
public  int deletegroupuser(String id);

}

