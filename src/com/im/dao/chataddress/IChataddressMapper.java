package com.im.dao.chataddress;
import java.util.List;
import java.util.Map;
import com.im.model.chataddress.Chataddress;
	public interface IChataddressMapper {
	/**
 	* 通过id选取
 	* @return
 	*/
	public Chataddress selectchataddressById(String id);
	/**
 	* 通过查询参数获取信息
 	* @return
 */ 
 @SuppressWarnings("rawtypes")
	public List<Chataddress> selectchataddressByParam(Map paramMap); 
	/**
		* 通过查询参数获取总条数
	    * @return
	*/ 
	@SuppressWarnings("rawtypes")
	public int selectCountchataddressByParam(Map paramMap); 
	/**
 	* 更新 
 	* @return 
 */ 
	public  int updatechataddress(Chataddress chataddress);
	public  int updateChatByUserid(Chataddress chataddress);
	/**
 	* 添加 
 	* @return
 */ 
	public  int addchataddress(Chataddress chataddress);
	/**
 	* 批量添加 
 	* @return
 */ 
	public  int muladdchataddress(List<Chataddress> list);
	/**
 	* 删除 
 	* @return 
 */ 
public  int deletechataddress(String id);

}

