package com.im.controller.chatgroup;
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
import com.im.model.chatgroup.Chatgroup;
import com.im.service.chatgroup.IChatgroupService;
@Controller
public class ChatgroupController {
	@Autowired
	private IChatgroupService iChatgroupService;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Logger logger = Logger.getLogger("IMLogger");
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/addChatgroup")
	@ResponseBody
	public Map add(Chatgroup chatgroup){
		Map resultMap=new HashMap();
		try {
			iChatgroupService.addChatgroup(chatgroup);
			resultMap.put("status", "0");
			resultMap.put("msg", chatgroup.getId());
			logger.info("新建成功，主键："+chatgroup.getId());
		} catch (Exception e) {
			resultMap.put("status", "-1");
			resultMap.put("msg", "新建失败！");
			logger.info("新建失败！"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/muladdChatgroup")
	@ResponseBody
	public Map muladd(HttpServletRequest request,Chatgroup chatgroup){
		Map resultMap=new HashMap();
		try {
			String data=request.getParameter("data");
			ObjectMapper objectMapper = new ObjectMapper();
			JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, Chatgroup.class);
			List<Chatgroup> list = (List<Chatgroup>)objectMapper.readValue(data, javaType);
			iChatgroupService.muladdChatgroup(list);
			resultMap.put("status", "0");
			resultMap.put("msg", "新建成功");
			logger.info("新建成功，主键："+chatgroup.getId());
		} catch (Exception e) {
			resultMap.put("status", "-1");
			resultMap.put("msg", "新建失败！");
			logger.info("新建失败！"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	@RequestMapping("/deleteChatgroup")
	@ResponseBody
	public Map delete(Chatgroup chatgroup){
		Map resultMap=new HashMap();
		try {
			if(chatgroup.getId()==null){
				resultMap.put("status", "-1");
				resultMap.put("msg", "参数不能为空！");
			}
			else{
				int resultDelete=iChatgroupService.deleteChatgroup(chatgroup.getId()+"");
				resultMap.put("status", "0");
				resultMap.put("msg", "删除成功！");
				logger.info("删除成功，主键："+chatgroup.getId());
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
	@RequestMapping("/selectChatgroup")
	@ResponseBody
	public Map select(Chatgroup chatgroup){
		Map resultMap=new HashMap();
		try {
			if(chatgroup.getId()==null){
				resultMap.put("status", "-1");
				resultMap.put("msg", "参数不能为空！");
			}
			else{
				Chatgroup resultSelect=iChatgroupService.selectChatgroupById(chatgroup.getId()+"");
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
	@RequestMapping("/updateChatgroup")
	@ResponseBody
	public Map update(Chatgroup chatgroup){
		Map resultMap=new HashMap();
		try {
			if(chatgroup.getId()==null){
				resultMap.put("status", "-1");
				resultMap.put("msg", "参数不能为空！");
			}
			else{
				int resultUpdate=iChatgroupService.updateChatgroup(chatgroup);
				resultMap.put("status", "0");
				resultMap.put("msg", "更新成功！");
				logger.info("更新成功，主键："+chatgroup.getId());
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
	@RequestMapping("/listChatgroup")
	@ResponseBody
	public Map list(HttpServletRequest request, HttpServletResponse response,Chatgroup chatgroup)
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
				paramMap.put("id",chatgroup.getId());
				paramMap.put("groupid",chatgroup.getGroupid());
				paramMap.put("groupname",chatgroup.getGroupname());
				paramMap.put("adduserid",chatgroup.getAdduserid());
				paramMap.put("groupimage",chatgroup.getGroupimage());
				paramMap.put("groupdetail",chatgroup.getGroupdetail());
				String addtimeFrom=request.getParameter("addtimeFrom");
				String addtimeTo=request.getParameter("addtimeTo");
				if(addtimeFrom!=null&&!addtimeFrom.equals(""))
				paramMap.put("addtimeFrom", sdf.parse(addtimeFrom));
				if(addtimeTo!=null&&!addtimeTo.equals(""))
				paramMap.put("addtimeTo", sdf.parse(addtimeTo));
				paramMap.put("flag",chatgroup.getFlag());
				List<Chatgroup> list=iChatgroupService.selectChatgroupByParam(paramMap);
				int totalnumber=iChatgroupService.selectCountChatgroupByParam(paramMap);
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
