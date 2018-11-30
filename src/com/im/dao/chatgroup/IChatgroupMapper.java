package com.im.dao.chatgroup;
import java.util.List;
import java.util.Map;
import com.im.model.chatgroup.Chatgroup;
	public interface IChatgroupMapper {
	/**
 	* 通过id选取
 	* @return
 	*/
	public Chatgroup selectchatgroupById(String id);
	/**
 	* 通过查询参数获取信息
 	* @return
 */ 
 @SuppressWarnings("rawtypes")
	public List<Chatgroup> selectchatgroupByParam(Map paramMap); 
	/**
		* 通过查询参数获取总条数
	    * @return
	*/ 
	@SuppressWarnings("rawtypes")
	public int selectCountchatgroupByParam(Map paramMap); 
	/**
 	* 更新 
 	* @return 
 */ 
	public  int updatechatgroup(Chatgroup chatgroup);
	/**
 	* 添加 
 	* @return
 */ 
	public  int addchatgroup(Chatgroup chatgroup);
	/**
 	* 批量添加 
 	* @return
 */ 
	public  int muladdchatgroup(List<Chatgroup> list);
	/**
 	* 删除 
 	* @return 
 */ 
public  int deletechatgroup(String id);

}

