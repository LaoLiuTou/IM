package com.im.controller.messagebackup;
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
import com.im.model.messagebackup.Messagebackup;
import com.im.service.messagebackup.IMessagebackupService;
@Controller
public class MessagebackupController {
	@Autowired
	private IMessagebackupService iMessagebackupService;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Logger logger = Logger.getLogger("IMLogger");
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/addMessagebackup")
	@ResponseBody
	public Map add(Messagebackup messagebackup){
		Map resultMap=new HashMap();
		try {
			iMessagebackupService.addMessagebackup(messagebackup);
			resultMap.put("status", "0");
			resultMap.put("msg", messagebackup.getId());
			logger.info("新建成功，主键："+messagebackup.getId());
		} catch (Exception e) {
			resultMap.put("status", "-1");
			resultMap.put("msg", "新建失败！");
			logger.info("新建失败！"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/muladdMessagebackup")
	@ResponseBody
	public Map muladd(HttpServletRequest request,Messagebackup messagebackup){
		Map resultMap=new HashMap();
		try {
			String data=request.getParameter("data");
			ObjectMapper objectMapper = new ObjectMapper();
			JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, Messagebackup.class);
			List<Messagebackup> list = (List<Messagebackup>)objectMapper.readValue(data, javaType);
			iMessagebackupService.muladdMessagebackup(list);
			resultMap.put("status", "0");
			resultMap.put("msg", "新建成功");
			logger.info("新建成功，主键："+messagebackup.getId());
		} catch (Exception e) {
			resultMap.put("status", "-1");
			resultMap.put("msg", "新建失败！");
			logger.info("新建失败！"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	@RequestMapping("/deleteMessagebackup")
	@ResponseBody
	public Map delete(Messagebackup messagebackup){
		Map resultMap=new HashMap();
		try {
			if(messagebackup.getId()==null){
				resultMap.put("status", "-1");
				resultMap.put("msg", "参数不能为空！");
			}
			else{
				int resultDelete=iMessagebackupService.deleteMessagebackup(messagebackup.getId()+"");
				resultMap.put("status", "0");
				resultMap.put("msg", "删除成功！");
				logger.info("删除成功，主键："+messagebackup.getId());
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
	@RequestMapping("/selectMessagebackup")
	@ResponseBody
	public Map select(Messagebackup messagebackup){
		Map resultMap=new HashMap();
		try {
			if(messagebackup.getId()==null){
				resultMap.put("status", "-1");
				resultMap.put("msg", "参数不能为空！");
			}
			else{
				Messagebackup resultSelect=iMessagebackupService.selectMessagebackupById(messagebackup.getId()+"");
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
	@RequestMapping("/updateMessagebackup")
	@ResponseBody
	public Map update(Messagebackup messagebackup){
		Map resultMap=new HashMap();
		try {
			if(messagebackup.getId()==null){
				resultMap.put("status", "-1");
				resultMap.put("msg", "参数不能为空！");
			}
			else{
				int resultUpdate=iMessagebackupService.updateMessagebackup(messagebackup);
				resultMap.put("status", "0");
				resultMap.put("msg", "更新成功！");
				logger.info("更新成功，主键："+messagebackup.getId());
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
	@RequestMapping("/listMessagebackup")
	@ResponseBody
	public Map list(HttpServletRequest request, HttpServletResponse response,Messagebackup messagebackup)
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
				paramMap.put("id",messagebackup.getId());
				paramMap.put("userid",messagebackup.getUserid());
				paramMap.put("friendid",messagebackup.getFriendid());
				String addtimeFrom=request.getParameter("addtimeFrom");
				String addtimeTo=request.getParameter("addtimeTo");
				if(addtimeFrom!=null&&!addtimeFrom.equals(""))
				paramMap.put("addtimeFrom", sdf.parse(addtimeFrom));
				if(addtimeTo!=null&&!addtimeTo.equals(""))
				paramMap.put("addtimeTo", sdf.parse(addtimeTo));
				paramMap.put("chattype",messagebackup.getChattype());
				paramMap.put("contenttype",messagebackup.getContenttype());
				paramMap.put("content",messagebackup.getContent());
				paramMap.put("readstatus",messagebackup.getReadstatus());
				paramMap.put("sendstatus",messagebackup.getSendstatus());
				paramMap.put("isgroup",messagebackup.getIsgroup());
				paramMap.put("flag",messagebackup.getFlag());
				List<Messagebackup> list=iMessagebackupService.selectMessagebackupByParam(paramMap);
				int totalnumber=iMessagebackupService.selectCountMessagebackupByParam(paramMap);
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
