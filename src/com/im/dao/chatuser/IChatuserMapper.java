package com.im.dao.chatuser;
import java.util.List;
import java.util.Map;
import com.im.model.chatuser.Chatuser;
	public interface IChatuserMapper {
	/**
 	* 通过id选取
 	* @return
 	*/
	public Chatuser selectchatuserById(String id);
	/**
 	* 通过查询参数获取信息
 	* @return
 */ 
 @SuppressWarnings("rawtypes")
	public List<Chatuser> selectchatuserByParam(Map paramMap); 
	/**
		* 通过查询参数获取总条数
	    * @return
	*/ 
	@SuppressWarnings("rawtypes")
	public int selectCountchatuserByParam(Map paramMap); 
	/**
 	* 更新 
 	* @return 
 */ 
	public  int updatechatuser(Chatuser chatuser);
	/**
 	* 添加 
 	* @return
 */ 
	public  int addchatuser(Chatuser chatuser);
	/**
 	* 批量添加 
 	* @return
 */ 
	public  int muladdchatuser(List<Chatuser> list);
	/**
 	* 删除 
 	* @return 
 */ 
public  int deletechatuser(String id);

}

