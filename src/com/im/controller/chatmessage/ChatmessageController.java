package com.im.controller.chatmessage;
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
import com.im.model.chatmessage.Chatmessage;
import com.im.service.chatmessage.IChatmessageService;
@Controller
public class ChatmessageController {
	@Autowired
	private IChatmessageService iChatmessageService;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Logger logger = Logger.getLogger("IMLogger");
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/addChatmessage")
	@ResponseBody
	public Map add(Chatmessage chatmessage){
		Map resultMap=new HashMap();
		try {
			iChatmessageService.addChatmessage(chatmessage);
			resultMap.put("status", "0");
			resultMap.put("msg", chatmessage.getId());
			logger.info("新建成功，主键："+chatmessage.getId());
		} catch (Exception e) {
			resultMap.put("status", "-1");
			resultMap.put("msg", "新建失败！");
			logger.info("新建失败！"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/muladdChatmessage")
	@ResponseBody
	public Map muladd(HttpServletRequest request,Chatmessage chatmessage){
		Map resultMap=new HashMap();
		try {
			String data=request.getParameter("data");
			ObjectMapper objectMapper = new ObjectMapper();
			JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, Chatmessage.class);
			List<Chatmessage> list = (List<Chatmessage>)objectMapper.readValue(data, javaType);
			iChatmessageService.muladdChatmessage(list);
			resultMap.put("status", "0");
			resultMap.put("msg", "新建成功");
			logger.info("新建成功，主键："+chatmessage.getId());
		} catch (Exception e) {
			resultMap.put("status", "-1");
			resultMap.put("msg", "新建失败！");
			logger.info("新建失败！"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	@RequestMapping("/deleteChatmessage")
	@ResponseBody
	public Map delete(Chatmessage chatmessage){
		Map resultMap=new HashMap();
		try {
			if(chatmessage.getId()==null){
				resultMap.put("status", "-1");
				resultMap.put("msg", "参数不能为空！");
			}
			else{
				int resultDelete=iChatmessageService.deleteChatmessage(chatmessage.getId()+"");
				resultMap.put("status", "0");
				resultMap.put("msg", "删除成功！");
				logger.info("删除成功，主键："+chatmessage.getId());
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
	@RequestMapping("/selectChatmessage")
	@ResponseBody
	public Map select(Chatmessage chatmessage){
		Map resultMap=new HashMap();
		try {
			if(chatmessage.getId()==null){
				resultMap.put("status", "-1");
				resultMap.put("msg", "参数不能为空！");
			}
			else{
				Chatmessage resultSelect=iChatmessageService.selectChatmessageById(chatmessage.getId()+"");
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
	@RequestMapping("/updateChatmessage")
	@ResponseBody
	public Map update(Chatmessage chatmessage){
		Map resultMap=new HashMap();
		try {
			if(chatmessage.getId()==null){
				resultMap.put("status", "-1");
				resultMap.put("msg", "参数不能为空！");
			}
			else{
				int resultUpdate=iChatmessageService.updateChatmessage(chatmessage);
				resultMap.put("status", "0");
				resultMap.put("msg", "更新成功！");
				logger.info("更新成功，主键："+chatmessage.getId());
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
	@RequestMapping("/listChatmessage")
	@ResponseBody
	public Map list(HttpServletRequest request, HttpServletResponse response,Chatmessage chatmessage)
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
				paramMap.put("id",chatmessage.getId());
				paramMap.put("userid",chatmessage.getUserid());
				paramMap.put("friendid",chatmessage.getFriendid());
				String addtimeFrom=request.getParameter("addtimeFrom");
				String addtimeTo=request.getParameter("addtimeTo");
				if(addtimeFrom!=null&&!addtimeFrom.equals(""))
				paramMap.put("addtimeFrom", sdf.parse(addtimeFrom));
				if(addtimeTo!=null&&!addtimeTo.equals(""))
				paramMap.put("addtimeTo", sdf.parse(addtimeTo));
				paramMap.put("chattype",chatmessage.getChattype());
				paramMap.put("contenttype",chatmessage.getContenttype());
				paramMap.put("content",chatmessage.getContent());
				paramMap.put("readstatus",chatmessage.getReadstatus());
				paramMap.put("sendstatus",chatmessage.getSendstatus());
				paramMap.put("isgroup",chatmessage.getIsgroup());
				paramMap.put("flag",chatmessage.getFlag());
				List<Chatmessage> list=iChatmessageService.selectChatmessageByParam(paramMap);
				int totalnumber=iChatmessageService.selectCountChatmessageByParam(paramMap);
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
