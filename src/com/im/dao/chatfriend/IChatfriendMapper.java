package com.im.dao.chatfriend;
import java.util.List;
import java.util.Map;
import com.im.model.chatfriend.Chatfriend;
	public interface IChatfriendMapper {
	/**
 	* 通过id选取
 	* @return
 	*/
	public Chatfriend selectchatfriendById(String id);
	/**
 	* 通过查询参数获取信息
 	* @return
 */ 
 @SuppressWarnings("rawtypes")
	public List<Chatfriend> selectchatfriendByParam(Map paramMap); 
	/**
		* 通过查询参数获取总条数
	    * @return
	*/ 
	@SuppressWarnings("rawtypes")
	public int selectCountchatfriendByParam(Map paramMap); 
	/**
 	* 更新 
 	* @return 
 */ 
	public  int updatechatfriend(Chatfriend chatfriend);
	/**
 	* 添加 
 	* @return
 */ 
	public  int addchatfriend(Chatfriend chatfriend);
	/**
 	* 批量添加 
 	* @return
 */ 
	public  int muladdchatfriend(List<Chatfriend> list);
	/**
 	* 删除 
 	* @return 
 */ 
public  int deletechatfriend(String id);

}

