package com.im.model.messagebackup;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
/**
 * @author LT
 */
public class Messagebackup {

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
	/** 添加时间 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private  Date addtime;
	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	/** 0-发送，1-接收 */
	private  String chattype;
	public String getChattype() {
		return chattype;
	}
	public void setChattype(String chattype) {
		this.chattype = chattype;
	}
	/** 0-文本，1-图片，2-语音，3-视频，4-文件 */
	private  String contenttype;
	public String getContenttype() {
		return contenttype;
	}
	public void setContenttype(String contenttype) {
		this.contenttype = contenttype;
	}
	/** 内容 */
	private  String content;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	/** 是否已读 0-yes 1-no */
	private  String readstatus;
	public String getReadstatus() {
		return readstatus;
	}
	public void setReadstatus(String readstatus) {
		this.readstatus = readstatus;
	}
	/** 是否发送成功 0-yes 1-no */
	private  String sendstatus;
	public String getSendstatus() {
		return sendstatus;
	}
	public void setSendstatus(String sendstatus) {
		this.sendstatus = sendstatus;
	}
	/** 0个人，1-分组 */
	private  String isgroup;
	public String getIsgroup() {
		return isgroup;
	}
	public void setIsgroup(String isgroup) {
		this.isgroup = isgroup;
	}
	/** 待用 */
	private  String flag;
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}



}
