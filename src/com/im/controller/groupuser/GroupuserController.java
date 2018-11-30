package com.im.controller.groupuser;
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
import com.im.model.groupuser.Groupuser;
import com.im.service.groupuser.IGroupuserService;
@Controller
public class GroupuserController {
	@Autowired
	private IGroupuserService iGroupuserService;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Logger logger = Logger.getLogger("IMLogger");
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/addGroupuser")
	@ResponseBody
	public Map add(Groupuser groupuser){
		Map resultMap=new HashMap();
		try {
			iGroupuserService.addGroupuser(groupuser);
			resultMap.put("status", "0");
			resultMap.put("msg", groupuser.getId());
			logger.info("新建成功，主键："+groupuser.getId());
		} catch (Exception e) {
			resultMap.put("status", "-1");
			resultMap.put("msg", "新建失败！");
			logger.info("新建失败！"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/muladdGroupuser")
	@ResponseBody
	public Map muladd(HttpServletRequest request,Groupuser groupuser){
		Map resultMap=new HashMap();
		try {
			String data=request.getParameter("data");
			ObjectMapper objectMapper = new ObjectMapper();
			JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, Groupuser.class);
			List<Groupuser> list = (List<Groupuser>)objectMapper.readValue(data, javaType);
			iGroupuserService.muladdGroupuser(list);
			resultMap.put("status", "0");
			resultMap.put("msg", "新建成功");
			logger.info("新建成功，主键："+groupuser.getId());
		} catch (Exception e) {
			resultMap.put("status", "-1");
			resultMap.put("msg", "新建失败！");
			logger.info("新建失败！"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	@RequestMapping("/deleteGroupuser")
	@ResponseBody
	public Map delete(Groupuser groupuser){
		Map resultMap=new HashMap();
		try {
			if(groupuser.getId()==null){
				resultMap.put("status", "-1");
				resultMap.put("msg", "参数不能为空！");
			}
			else{
				int resultDelete=iGroupuserService.deleteGroupuser(groupuser.getId()+"");
				resultMap.put("status", "0");
				resultMap.put("msg", "删除成功！");
				logger.info("删除成功，主键："+groupuser.getId());
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
	@RequestMapping("/selectGroupuser")
	@ResponseBody
	public Map select(Groupuser groupuser){
		Map resultMap=new HashMap();
		try {
			if(groupuser.getId()==null){
				resultMap.put("status", "-1");
				resultMap.put("msg", "参数不能为空！");
			}
			else{
				Groupuser resultSelect=iGroupuserService.selectGroupuserById(groupuser.getId()+"");
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
	@RequestMapping("/updateGroupuser")
	@ResponseBody
	public Map update(Groupuser groupuser){
		Map resultMap=new HashMap();
		try {
			if(groupuser.getId()==null){
				resultMap.put("status", "-1");
				resultMap.put("msg", "参数不能为空！");
			}
			else{
				int resultUpdate=iGroupuserService.updateGroupuser(groupuser);
				resultMap.put("status", "0");
				resultMap.put("msg", "更新成功！");
				logger.info("更新成功，主键："+groupuser.getId());
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
	@RequestMapping("/listGroupuser")
	@ResponseBody
	public Map list(HttpServletRequest request, HttpServletResponse response,Groupuser groupuser)
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
				paramMap.put("id",groupuser.getId());
				paramMap.put("groupid",groupuser.getGroupid());
				paramMap.put("userid",groupuser.getUserid());
				List<Groupuser> list=iGroupuserService.selectGroupuserByParam(paramMap);
				int totalnumber=iGroupuserService.selectCountGroupuserByParam(paramMap);
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
