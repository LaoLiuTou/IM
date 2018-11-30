package com.im.controller.chatfriend;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.im.model.chatfriend.Chatfriend;
import com.im.service.chatfriend.IChatfriendService;
@Controller
public class ChatfriendController {
	@Autowired
	private IChatfriendService iChatfriendService;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Logger logger = Logger.getLogger("IMLogger");
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/addChatfriend")
	@ResponseBody
	public Map add(Chatfriend chatfriend){
		Map resultMap=new HashMap();
		try {
			iChatfriendService.addChatfriend(chatfriend);
			resultMap.put("status", "0");
			resultMap.put("msg", chatfriend.getId());
			logger.info("新建成功，主键："+chatfriend.getId());
		} catch (Exception e) {
			resultMap.put("status", "-1");
			resultMap.put("msg", "新建失败！");
			logger.info("新建失败！"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/muladdChatfriend")
	@ResponseBody
	public Map muladd(HttpServletRequest request,Chatfriend chatfriend){
		Map resultMap=new HashMap();
		try {
			String data=request.getParameter("data");
			ObjectMapper objectMapper = new ObjectMapper();
			JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, Chatfriend.class);
			List<Chatfriend> list = (List<Chatfriend>)objectMapper.readValue(data, javaType);
			iChatfriendService.muladdChatfriend(list);
			resultMap.put("status", "0");
			resultMap.put("msg", "新建成功");
			logger.info("新建成功，主键："+chatfriend.getId());
		} catch (Exception e) {
			resultMap.put("status", "-1");
			resultMap.put("msg", "新建失败！");
			logger.info("新建失败！"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	@RequestMapping("/deleteChatfriend")
	@ResponseBody
	public Map delete(Chatfriend chatfriend){
		Map resultMap=new HashMap();
		try {
			if(chatfriend.getId()==null){
				resultMap.put("status", "-1");
				resultMap.put("msg", "参数不能为空！");
			}
			else{
				int resultDelete=iChatfriendService.deleteChatfriend(chatfriend.getId()+"");
				resultMap.put("status", "0");
				resultMap.put("msg", "删除成功！");
				logger.info("删除成功，主键："+chatfriend.getId());
			}
		} catch (Exception e) {
			resultMap.put("status", "-1");
			resultMap.put("msg", "删除失败！");
			logger.info("删除失败！"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/selectChatfriend")
	@ResponseBody
	public Map select(Chatfriend chatfriend){
		Map resultMap=new HashMap();
		try {
			if(chatfriend.getId()==null){
				resultMap.put("status", "-1");
				resultMap.put("msg", "参数不能为空！");
			}
			else{
				Chatfriend resultSelect=iChatfriendService.selectChatfriendById(chatfriend.getId()+"");
				resultMap.put("status", "0");
				resultMap.put("msg", resultSelect);
			}
		} catch (Exception e) {
			resultMap.put("status", "-1");
			resultMap.put("msg", "查询失败！");
			logger.info("查询失败！"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	@RequestMapping("/updateChatfriend")
	@ResponseBody
	public Map update(Chatfriend chatfriend){
		Map resultMap=new HashMap();
		try {
			if(chatfriend.getId()==null){
				resultMap.put("status", "-1");
				resultMap.put("msg", "参数不能为空！");
			}
			else{
				int resultUpdate=iChatfriendService.updateChatfriend(chatfriend);
				resultMap.put("status", "0");
				resultMap.put("msg", "更新成功！");
				logger.info("更新成功，主键："+chatfriend.getId());
			}
		} catch (Exception e) {
			resultMap.put("status", "-1");
			resultMap.put("msg", "更新失败！");
			logger.info("更新失败！"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/listChatfriend")
	@ResponseBody
	public Map list(HttpServletRequest request, HttpServletResponse response,Chatfriend chatfriend)
		throws ServletException, IOException {
		Map resultMap=new HashMap();
		try {
			String page=request.getParameter("page");
			String size=request.getParameter("size");
			if(page!=null&&size!=null){
				Map paramMap=new HashMap();
				paramMap.put("fromPage",(Integer.parseInt(page)-1)*Integer.parseInt(size));
				paramMap.put("toPage",Integer.parseInt(size)); 
				paramMap.put("orderBy","ID DESC"); 
				paramMap.put("id",chatfriend.getId());
				paramMap.put("userid",chatfriend.getUserid());
				paramMap.put("friendid",chatfriend.getFriendid());
				paramMap.put("isgroup",chatfriend.getIsgroup());
				paramMap.put("isonline",chatfriend.getIsonline());
				List<Chatfriend> list=iChatfriendService.selectChatfriendByParam(paramMap);
				int totalnumber=iChatfriendService.selectCountChatfriendByParam(paramMap);
				Map tempMap=new HashMap();
				resultMap.put("status", "0");
				tempMap.put("num", totalnumber);
				tempMap.put("data", list);
				resultMap.put("msg", tempMap);
			}
			else{
				resultMap.put("status", "-1");
				resultMap.put("msg", "分页参数不能为空！");
			}
		} catch (Exception e) {
			resultMap.put("status", "-1");
			resultMap.put("msg", "查询失败！");
			logger.info("查询失败！"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
}
