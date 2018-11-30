package com.im.model.chatfriend;
/**
 * @author LT
 */
public class Chatfriend {

	/**  */
	private  Long id;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	/** 用户id */
	private  String userid;
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	/** 聊天人id */
	private  String friendid;
	public String getFriendid() {
		return friendid;
	}
	public void setFriendid(String friendid) {
		this.friendid = friendid;
	}
	/** 是否是群聊 0-yes 1--no */
	private  String isgroup;
	public String getIsgroup() {
		return isgroup;
	}
	public void setIsgroup(String isgroup) {
		this.isgroup = isgroup;
	}
	/** 是否在线 0-yes 1- no */
	private  String isonline;
	public String getIsonline() {
		return isonline;
	}
	public void setIsonline(String isonline) {
		this.isonline = isonline;
	}



}
