package com.im.controller.chatuser;
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
import com.im.model.chatuser.Chatuser;
import com.im.service.chatuser.IChatuserService;
@Controller
public class ChatuserController {
	@Autowired
	private IChatuserService iChatuserService;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Logger logger = Logger.getLogger("IMLogger");
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/addChatuser")
	@ResponseBody
	public Map add(Chatuser chatuser){
		Map resultMap=new HashMap();
		try {
			iChatuserService.addChatuser(chatuser);
			resultMap.put("status", "0");
			resultMap.put("msg", chatuser.getId());
			logger.info("新建成功，主键："+chatuser.getId());
		} catch (Exception e) {
			resultMap.put("status", "-1");
			resultMap.put("msg", "新建失败！");
			logger.info("新建失败！"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/muladdChatuser")
	@ResponseBody
	public Map muladd(HttpServletRequest request,Chatuser chatuser){
		Map resultMap=new HashMap();
		try {
			String data=request.getParameter("data");
			ObjectMapper objectMapper = new ObjectMapper();
			JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, Chatuser.class);
			List<Chatuser> list = (List<Chatuser>)objectMapper.readValue(data, javaType);
			iChatuserService.muladdChatuser(list);
			resultMap.put("status", "0");
			resultMap.put("msg", "新建成功");
			logger.info("新建成功，主键："+chatuser.getId());
		} catch (Exception e) {
			resultMap.put("status", "-1");
			resultMap.put("msg", "新建失败！");
			logger.info("新建失败！"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	@RequestMapping("/deleteChatuser")
	@ResponseBody
	public Map delete(Chatuser chatuser){
		Map resultMap=new HashMap();
		try {
			if(chatuser.getId()==null){
				resultMap.put("status", "-1");
				resultMap.put("msg", "参数不能为空！");
			}
			else{
				int resultDelete=iChatuserService.deleteChatuser(chatuser.getId()+"");
				resultMap.put("status", "0");
				resultMap.put("msg", "删除成功！");
				logger.info("删除成功，主键："+chatuser.getId());
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
	@RequestMapping("/selectChatuser")
	@ResponseBody
	public Map select(Chatuser chatuser){
		Map resultMap=new HashMap();
		try {
			if(chatuser.getId()==null){
				resultMap.put("status", "-1");
				resultMap.put("msg", "参数不能为空！");
			}
			else{
				Chatuser resultSelect=iChatuserService.selectChatuserById(chatuser.getId()+"");
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
	@RequestMapping("/updateChatuser")
	@ResponseBody
	public Map update(Chatuser chatuser){
		Map resultMap=new HashMap();
		try {
			if(chatuser.getId()==null){
				resultMap.put("status", "-1");
				resultMap.put("msg", "参数不能为空！");
			}
			else{
				int resultUpdate=iChatuserService.updateChatuser(chatuser);
				resultMap.put("status", "0");
				resultMap.put("msg", "更新成功！");
				logger.info("更新成功，主键："+chatuser.getId());
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
	@RequestMapping("/listChatuser")
	@ResponseBody
	public Map list(HttpServletRequest request, HttpServletResponse response,Chatuser chatuser)
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
				paramMap.put("id",chatuser.getId());
				paramMap.put("userid",chatuser.getUserid());
				paramMap.put("username",chatuser.getUsername());
				paramMap.put("userimage",chatuser.getUserimage());
				paramMap.put("detail",chatuser.getDetail());
				String addtimeFrom=request.getParameter("addtimeFrom");
				String addtimeTo=request.getParameter("addtimeTo");
				if(addtimeFrom!=null&&!addtimeFrom.equals(""))
				paramMap.put("addtimeFrom", sdf.parse(addtimeFrom));
				if(addtimeTo!=null&&!addtimeTo.equals(""))
				paramMap.put("addtimeTo", sdf.parse(addtimeTo));
				paramMap.put("isonline",chatuser.getIsonline());
				paramMap.put("flag",chatuser.getFlag());
				List<Chatuser> list=iChatuserService.selectChatuserByParam(paramMap);
				int totalnumber=iChatuserService.selectCountChatuserByParam(paramMap);
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
