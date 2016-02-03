package com.pujjr.web.controller;

import java.io.File;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pujjr.custom.Result;
import com.pujjr.custom.Utils;
import com.pujjr.domain.SmsSended;
import com.pujjr.domain.SmsTaskDtl;
import com.pujjr.domain.SmsTaskInfo;
import com.pujjr.domain.SysAccount;
import com.pujjr.domain.Template;
import com.pujjr.domain.TplDtl;
import com.pujjr.service.SmsService;

@Controller
@RequestMapping("/pujjr")
public class SmsController
{
	@Resource
	private SmsService smsService;
	@RequestMapping("/queryTemplateList")
	@ResponseBody
	public Result queryTemplateList(String name ,
									@RequestParam(value="page")String startPage,
									@RequestParam(value="limit")String pageSize,
									HttpSession session)
	{
		SysAccount sysAccount=(SysAccount)session.getAttribute("user");
		PageHelper.startPage(Integer.parseInt(startPage), Integer.parseInt(pageSize),true);
		List<Template> list=smsService.queryTemplateList(name);
		Result result=new Result(true);
		result.setTotalsize( String.valueOf(((Page)list).getTotal()));
		result.setItems(list);
		return result;
	}
	@RequestMapping("/addTemplate")
	@ResponseBody
	public Result addTemplate(String name,
							  String content,
							  int startColNum,
							  String telVarName,
							  MultipartFile attachFile,
							  String detailStr,
							  HttpSession session)
	{
		try
		{
			String newFileName=name+"模板"+Utils.getFileSuffix(attachFile.getOriginalFilename());
			String absFilePath=session.getServletContext().getRealPath("/")+"template"+File.separator+newFileName;
			FileUtils.copyInputStreamToFile(attachFile.getInputStream(), new File(absFilePath));
			SysAccount sysAccount=(SysAccount)session.getAttribute("user");
			Template template=new Template();
			String tplId=Utils.get16UUID();
			template.setId(tplId);
			template.setName(name);
			template.setContent(content);
			template.setStartcolnum(startColNum);
			template.setTelvarname(telVarName);
			template.setAttachname(name+"模板");
			template.setAttachpath("template"+File.separator+newFileName);
			template.setModifyid(sysAccount.getAccountid());
			template.setModifydate(new Date());
			ArrayList<TplDtl> detailList=new ArrayList<TplDtl>();
			String tmp[]=detailStr.substring(0, detailStr.length()-1).split("#");
			for(int i=0;i<tmp.length;i++)
			{
				String detail[]=tmp[i].toString().split(",");
				TplDtl tplDtl=new TplDtl();
				tplDtl.setId(tplId);
				tplDtl.setSeq(Integer.valueOf(detail[0]));
				tplDtl.setComment(detail[1]);
				tplDtl.setVarname(detail[2]);
				tplDtl.setColnum(detail[3]);
				detailList.add(tplDtl);
			}
			smsService.addTemplate(template, detailList);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Result result=new Result(false);
			result.addErros("errmsg", e.getMessage());
			return result;
		}
		return new Result(true);
	}
	
	@RequestMapping("/deleteTemplate")
	@ResponseBody
	public Result deleteTemplate(String id,
								 HttpSession session)
	{
		try
		{
			smsService.deleteTemplate(id);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Result result=new Result(false);
			result.addErros("errmsg", e.getMessage());
			return result;
		}
		return new Result(true);
	}

	@RequestMapping("/updateTemplate")
	@ResponseBody
	public Result updateTemplate(String id,
								 String name,
							   	 String content,
							   	 int startColNum,
							   	 String telVarName,
							   	 MultipartFile attachFile,
							   	 String detailStr,
							     HttpSession session)
	{
		try
		{
			SysAccount sysAccount=(SysAccount)session.getAttribute("user");
			Template template=smsService.queryTemplateById(id);
			if(template==null)
			{
				throw new Exception("修改失败,短信模板不存在");
			}
			template.setId(id);
			template.setName(name);
			template.setContent(content);
			template.setStartcolnum(startColNum);
			template.setTelvarname(telVarName);
			if(attachFile!=null)
			{
				String newFileName=name+"模板"+Utils.getFileSuffix(attachFile.getOriginalFilename());
				String absFilePath=session.getServletContext().getRealPath("/")+"template"+File.separator+newFileName;
				FileUtils.copyInputStreamToFile(attachFile.getInputStream(), new File(absFilePath));
				template.setAttachname(name+"模板");
				template.setAttachpath("template"+File.separator+newFileName);
			}
			template.setModifyid(sysAccount.getAccountid());
			template.setModifydate(new Date());
			ArrayList<TplDtl> detailList=new ArrayList<TplDtl>();
			String tmp[]=detailStr.substring(0, detailStr.length()-1).split("#");
			for(int i=0;i<tmp.length;i++)
			{
				String detail[]=tmp[i].toString().split(",");
				TplDtl tplDtl=new TplDtl();
				tplDtl.setId(id);
				tplDtl.setSeq(Integer.valueOf(detail[0]));
				tplDtl.setComment(detail[1]);
				tplDtl.setVarname(detail[2]);
				tplDtl.setColnum(detail[3]);
				detailList.add(tplDtl);
			}
			smsService.updateTemplate(template, detailList);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Result result=new Result(false);
			result.addErros("errmsg", e.getMessage());
			return result;
		}
		return new Result(true);
	}
	@RequestMapping("/queryTaskList")
	@ResponseBody
	public Result queryTaskList(   String taskname ,
								   String createdate,
									@RequestParam(value="page")String startPage,
									@RequestParam(value="limit")String pageSize,
									HttpSession session) throws ParseException
	{
		SysAccount sysAccount=(SysAccount)session.getAttribute("user");
		PageHelper.startPage(Integer.parseInt(startPage), Integer.parseInt(pageSize),true);
		Timestamp starttime=createdate.equals("")?null:Utils.str2time(createdate+" 00:00");
		Timestamp endtime=createdate.equals("")?null:Utils.str2time(createdate+" 23:59");
		List<SmsTaskInfo> list=smsService.queryTaskList(taskname, starttime,endtime,sysAccount.getAccountid());
		Result result=new Result(true);
		result.setTotalsize( String.valueOf(((Page)list).getTotal()));
		result.setItems(list);
		return result;
	}
	@RequestMapping("/queryTaskDtlList")
	@ResponseBody
	public Result queryTaskDtlList( String taskid ,
									@RequestParam(value="page")String startPage,
									@RequestParam(value="limit")String pageSize,
									HttpSession session) throws ParseException
	{
		PageHelper.startPage(Integer.parseInt(startPage), Integer.parseInt(pageSize),true);
		List<SmsTaskDtl> list=smsService.querySmsTaskDtlListByTaskId(taskid);
		Result result=new Result(true);
		result.setTotalsize( String.valueOf(((Page)list).getTotal()));
		result.setItems(list);
		return result;
	}
	@RequestMapping("/querySmsSendedList")
	@ResponseBody
	public Result querySmsSendedList( String content ,
			                          String tel,
			                          String startdate,
			                          String enddate,
			                          String status,
									@RequestParam(value="page")String startPage,
									@RequestParam(value="limit")String pageSize,
									HttpSession session) throws ParseException
	{
		PageHelper.startPage(Integer.parseInt(startPage), Integer.parseInt(pageSize),true);
		Timestamp starttime=startdate.equals("")?null:Utils.str2time(startdate+" 00:00:00");
		Timestamp endtime=enddate.equals("")?null:Utils.str2time(enddate+" 23:59:59");
		List<SmsSended> list=smsService.querySmsSendedList(content, tel,starttime,endtime,status);
		Result result=new Result(true);
		result.setTotalsize( String.valueOf(((Page)list).getTotal()));
		result.setItems(list);
		return result;
	}
	@RequestMapping("/confirmBatchSendMessageTask")
	@ResponseBody
	public Result confirmBatchSendMessageTask( String taskid) throws ParseException
	{
		try
		{
			smsService.moveTaskToWaitSend(taskid);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Result result=new Result(false);
			result.addErros("errmsg",e.getMessage());
			return result;
		}
		return new Result(true);
	}
	@RequestMapping("/createBatchSendMessageTask")
	@ResponseBody
	public Result createBatchSendMessageTask(MultipartFile file,
			 						String taskname,
			 						String tasktype,
			 						String tplid,
			 						String content,
			 						boolean istimertask,
			 						String senddate,
			 						String sendtime,
							        HttpSession session)
	{
		String taskId="";
		try
		{
			SysAccount sysAccount=(SysAccount)session.getAttribute("user"); 
			String jobId=Utils.get16UUID();
			String newFileName=jobId+Utils.getFileSuffix(file.getOriginalFilename());
			String absFilePath=session.getServletContext().getRealPath("/")+"upload"+File.separator+newFileName;
			FileUtils.copyInputStreamToFile(file.getInputStream(), new File(absFilePath));
			Timestamp time=null;
			if(istimertask)
			{
				if(senddate.equals("")||sendtime.equals(""))
				{
					throw new Exception("定时发送请选择发送日期和时间");
				}
				
				time=Utils.str2time(senddate+" "+sendtime);
				if(time.before(new Date()))
				{
					throw new Exception("定时发送时间应大于当前时间");
				}

			}
			taskId=smsService.batchSendMessage(absFilePath, taskname, tasktype, tplid, content, istimertask, time, sysAccount.getAccountid());
		}catch(Exception e)
		{
			e.printStackTrace();
			Result result=new Result(false);
			result.addErros("errmsg",e.getMessage());
			return result;
		}
		Result result=new Result(true);
		Map<String,String> item=new HashMap<String,String>();
		item.put("taskid", taskId);
		result.setItems(item);
		return result;
							     
	}
	@RequestMapping("/deleteBatchTask")
	@ResponseBody
	public Result deleteBatchTask(String taskid)
	{
		try
		{
			smsService.deleteBatchSendMessageTask(taskid);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Result result=new Result(false);
			result.addErros("errmsg",e.getMessage());
			return result;
		}
		return new Result(true);
	}
	
	
}
