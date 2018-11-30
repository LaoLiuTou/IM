package com.im.dao.messagebackup;
import java.util.List;
import java.util.Map;
import com.im.model.messagebackup.Messagebackup;
	public interface IMessagebackupMapper {
	/**
 	* 通过id选取
 	* @return
 	*/
	public Messagebackup selectmessagebackupById(String id);
	/**
 	* 通过查询参数获取信息
 	* @return
 */ 
 @SuppressWarnings("rawtypes")
	public List<Messagebackup> selectmessagebackupByParam(Map paramMap); 
	/**
		* 通过查询参数获取总条数
	    * @return
	*/ 
	@SuppressWarnings("rawtypes")
	public int selectCountmessagebackupByParam(Map paramMap); 
	/**
 	* 更新 
 	* @return 
 */ 
	public  int updatemessagebackup(Messagebackup messagebackup);
	/**
 	* 添加 
 	* @return
 */ 
	public  int addmessagebackup(Messagebackup messagebackup);
	/**
 	* 批量添加 
 	* @return
 */ 
	public  int muladdmessagebackup(List<Messagebackup> list);
	/**
 	* 删除 
 	* @return 
 */ 
public  int deletemessagebackup(String id);

}

