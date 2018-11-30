package com.im.model.chatgroup;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
/**
 * @author LT
 */
public class Chatgroup {

	/**  */
	private  Long id;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	/** 分组id */
	private  String groupid;
	public String getGroupid() {
		return groupid;
	}
	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}
	/** 分组名 */
	private  String groupname;
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	/** 创建者id */
	private  String adduserid;
	public String getAdduserid() {
		return adduserid;
	}
	public void setAdduserid(String adduserid) {
		this.adduserid = adduserid;
	}
	/** 图片 */
	private  String groupimage;
	public String getGroupimage() {
		return groupimage;
	}
	public void setGroupimage(String groupimage) {
		this.groupimage = groupimage;
	}
	/** 详情 */
	private  String groupdetail;
	public String getGroupdetail() {
		return groupdetail;
	}
	public void setGroupdetail(String groupdetail) {
		this.groupdetail = groupdetail;
	}
	/** 添加时间 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private  Date addtime;
	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	/** 待用 */
	private  Long flag;
	public Long getFlag() {
		return flag;
	}
	public void setFlag(Long flag) {
		this.flag = flag;
	}



}
