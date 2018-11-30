package com.im.controller.chataddress;
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
import com.im.model.chataddress.Chataddress;
import com.im.service.chataddress.IChataddressService;
@Controller
public class ChataddressController {
	@Autowired
	private IChataddressService iChataddressService;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Logger logger = Logger.getLogger("IMLogger");
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/addChataddress")
	@ResponseBody
	public Map add(Chataddress chataddress){
		Map resultMap=new HashMap();
		try {
			iChataddressService.addChataddress(chataddress);
			resultMap.put("status", "0");
			resultMap.put("msg", chataddress.getId());
			logger.info("新建成功，主键："+chataddress.getId());
		} catch (Exception e) {
			resultMap.put("status", "-1");
			resultMap.put("msg", "新建失败！");
			logger.info("新建失败！"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/muladdChataddress")
	@ResponseBody
	public Map muladd(HttpServletRequest request,Chataddress chataddress){
		Map resultMap=new HashMap();
		try {
			String data=request.getParameter("data");
			ObjectMapper objectMapper = new ObjectMapper();
			JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, Chataddress.class);
			List<Chataddress> list = (List<Chataddress>)objectMapper.readValue(data, javaType);
			iChataddressService.muladdChataddress(list);
			resultMap.put("status", "0");
			resultMap.put("msg", "新建成功");
			logger.info("新建成功，主键："+chataddress.getId());
		} catch (Exception e) {
			resultMap.put("status", "-1");
			resultMap.put("msg", "新建失败！");
			logger.info("新建失败！"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	@RequestMapping("/deleteChataddress")
	@ResponseBody
	public Map delete(Chataddress chataddress){
		Map resultMap=new HashMap();
		try {
			if(chataddress.getId()==null){
				resultMap.put("status", "-1");
				resultMap.put("msg", "参数不能为空！");
			}
			else{
				int resultDelete=iChataddressService.deleteChataddress(chataddress.getId()+"");
				resultMap.put("status", "0");
				resultMap.put("msg", "删除成功！");
				logger.info("删除成功，主键："+chataddress.getId());
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
	@RequestMapping("/selectChataddress")
	@ResponseBody
	public Map select(Chataddress chataddress){
		Map resultMap=new HashMap();
		try {
			if(chataddress.getId()==null){
				resultMap.put("status", "-1");
				resultMap.put("msg", "参数不能为空！");
			}
			else{
				Chataddress resultSelect=iChataddressService.selectChataddressById(chataddress.getId()+"");
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
	@RequestMapping("/updateChataddress")
	@ResponseBody
	public Map update(Chataddress chataddress){
		Map resultMap=new HashMap();
		try {
			if(chataddress.getId()==null){
				resultMap.put("status", "-1");
				resultMap.put("msg", "参数不能为空！");
			}
			else{
				int resultUpdate=iChataddressService.updateChataddress(chataddress);
				resultMap.put("status", "0");
				resultMap.put("msg", "更新成功！");
				logger.info("更新成功，主键："+chataddress.getId());
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
	@RequestMapping("/listChataddress")
	@ResponseBody
	public Map list(HttpServletRequest request, HttpServletResponse response,Chataddress chataddress)
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
				paramMap.put("id",chataddress.getId());
				paramMap.put("userid",chataddress.getUserid());
				paramMap.put("address",chataddress.getAddress());
				paramMap.put("status",chataddress.getStatus());
				String addtimeFrom=request.getParameter("addtimeFrom");
				String addtimeTo=request.getParameter("addtimeTo");
				if(addtimeFrom!=null&&!addtimeFrom.equals(""))
				paramMap.put("addtimeFrom", sdf.parse(addtimeFrom));
				if(addtimeTo!=null&&!addtimeTo.equals(""))
				paramMap.put("addtimeTo", sdf.parse(addtimeTo));
				List<Chataddress> list=iChataddressService.selectChataddressByParam(paramMap);
				int totalnumber=iChataddressService.selectCountChataddressByParam(paramMap);
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
