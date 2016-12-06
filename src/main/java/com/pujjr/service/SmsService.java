package com.pujjr.service;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pujjr.custom.Utils;
import com.pujjr.dao.SmsSendedMapper;
import com.pujjr.dao.SmsTaskDtlMapper;
import com.pujjr.dao.SmsTaskInfoMapper;
import com.pujjr.dao.SmsWaitSendMapper;
import com.pujjr.dao.TemplateMapper;
import com.pujjr.dao.TplDtlMapper;
import com.pujjr.domain.SmsSended;
import com.pujjr.domain.SmsTaskDtl;
import com.pujjr.domain.SmsTaskInfo;
import com.pujjr.domain.SmsWaitSend;
import com.pujjr.domain.Template;
import com.pujjr.domain.TplDtl;
import com.pujjr.merchant.SmsMerchant;

@Service
@Transactional(rollbackFor=Exception.class) 
public class SmsService
{
	@Resource
	private TemplateMapper templateDao;
	@Resource
	private TplDtlMapper tplDtlDao;
	@Resource
	private SmsTaskInfoMapper smsTaskInfoDao;
	@Resource
	private SmsTaskDtlMapper smsTaskDtlDao;
	@Resource
	private SmsWaitSendMapper smsWaitSendDao;
	@Resource
	private SmsSendedMapper smsSendedDao;
	@Resource
	private SmsMerchant smsMerchant;
	/**
	 * 根据ID查询模板信息
	 * **/
	public Template queryTemplateById(String id)
	{
		return templateDao.selectByPrimaryKey(id);
	}
	/**
	 * 功能：查询短信模板列表
	 * 参数：name-模板名称
	 * 返回：模板列表信息
	 * **/
	public List<Template> queryTemplateList(String name)
	{
		return templateDao.selectList(name);
	}
	private void checkTemplateIsValid(Template template,List<TplDtl> detailList) throws Exception
	{
		String content=template.getContent();
		int varcnt=0;
		int startIndex=0;
		int endIndex=0;
		while(true)
		{
			startIndex=content.indexOf("#{", startIndex);
			if(startIndex==-1)
			{
				break;
			}
			endIndex=content.indexOf("}", startIndex+2);
			if(endIndex==-1)
			{
				throw new Exception("模板内容中变量定义缺失右括号");
			}
				
			String varname=content.substring(startIndex+2, endIndex);
			boolean hasDefineVar=false;
			for(int j=0;j<detailList.size();j++)
			{
				TplDtl item=detailList.get(j);
				if(item.getVarname().equals(varname))
				{
					hasDefineVar=true;
				}
			}
			if(!hasDefineVar)
			{
				throw new Exception("变量"+varname+"未定义");
			}
			varcnt++;
			startIndex=endIndex+1;
		}
		//这里要把接收号码也算进去
		if((varcnt+1)!=detailList.size())
		{
			throw new Exception("模板变量个数与定义的变量个数不符");
		}
		boolean hasDefineVar=false;
		for(int j=0;j<detailList.size();j++)
		{
			TplDtl item=detailList.get(j);
			if(item.getVarname().equals(template.getTelvarname()))
			{
				hasDefineVar=true;
			}
		}
		if(!hasDefineVar)
		{
			throw new Exception("变量"+template.getTelvarname()+"未定义");
		}
	}
	 /**
	  * 功能：添加模板
	  * 参数：template-模板信息
	  *     detaiList-模板明细信息
	  * 返回：无
	  * **/
	public void addTemplate(Template template,List<TplDtl> detailList) throws Exception
	{
		try
		{
			checkTemplateIsValid(template,detailList);
			templateDao.insert(template);
			for(int i=0;i<detailList.size();i++)
			{
				tplDtlDao.insert(detailList.get(i));
			}
		}catch(Exception e)
		{
			throw new Exception(e.getMessage());
		}
	}
	/**
	 * 功能：删除短信模板
	 * 参数：id-模板编号
	 * 返回：无
	 * @throws Exception 
	 * **/
	public void deleteTemplate(String id) throws Exception
	{
		try
		{
			tplDtlDao.deleteByTemplateId(id);
			templateDao.deleteByPrimaryKey(id);
		}catch(Exception e)
		{
			throw new Exception(e.getMessage());
		}
	}
	/**
	 * 功能：更新短信模板信息
	 * 参数：template-模板信息
	 *     detailList-模板明细信息
	 * **/
	public void updateTemplate(Template template,List<TplDtl> detailList) throws Exception
	{
		try
		{
			checkTemplateIsValid(template,detailList);
			tplDtlDao.deleteByTemplateId(template.getId());
			templateDao.updateByPrimaryKey(template);
			for(int i=0;i<detailList.size();i++)
			{
				tplDtlDao.insert(detailList.get(i));
			}
		}catch(Exception e)
		{
			throw new Exception(e.getMessage());
		}
	}
	/**
	 * 功能：查询批量短信任务列表
	 * 参数： taskname-任务名称
	 *     createdate-任务创建日期
	 * 返回：批量任务列表
	 * **/
	public List<SmsTaskInfo>  queryTaskList(String taskname,Timestamp starttime,Timestamp endtime,String createid)
	{
		return smsTaskInfoDao.selectList(taskname, starttime,endtime,createid);
	}
	
	/**
	 * 功能：根据模板批量发送短信
	 * @throws Exception 
	 *  
	 * **/
	public String batchSendMessage(String filePath,
								 String taskName,
								 String taskType,
								 String tplId,
								 String content,
								 boolean isTimerTask,
								 Timestamp sendTime,
								 String createId) throws Exception
	{
		//保存任务信息
		String taskId=Utils.get16UUID();
		try
		{
			SmsTaskInfo taskInfo=new SmsTaskInfo();
			taskInfo.setTaskid(taskId);
			taskInfo.setTaskname(taskName);
			taskInfo.setTasktype(taskType);
			taskInfo.setTplid(tplId);
			taskInfo.setContent(content);
			taskInfo.setFilename(filePath);
			taskInfo.setIstimertask(isTimerTask);
			taskInfo.setSendtime(sendTime);
			taskInfo.setCreateid(createId);
			taskInfo.setCreatedate(new Timestamp(new Date().getTime()));
			taskInfo.setProcstatus("");
			smsTaskInfoDao.insert(taskInfo);
			int totalCnt=generateTaskDetail(filePath,taskInfo);
			//checkSmsMerchantAvailable(totalCnt);
			taskInfo.setTotalcnt(totalCnt);
			if(isTimerTask)
				taskInfo.setProcstatus("待处理");
			else
				taskInfo.setProcstatus("待确认");
			smsTaskInfoDao.updateByPrimaryKey(taskInfo);
			
			
		}catch(Exception e)
		{
			throw new Exception(e.getMessage());
		}
		return taskId;
		
	}
	/**
	 * 功能：根据文件生成任务明细信息
	 * 参数：filePath-文件路径
	 *     taskInfo-任务信息
	 * 返回：任务总笔数
	 * **/
	private int generateTaskDetail(String filePath,SmsTaskInfo taskInfo) throws Exception
	{
		//读取模板信息
		Template template = templateDao.selectByPrimaryKey(taskInfo.getTplid());
		if (template == null)
		{
			throw new Exception("短信模板id[" + taskInfo.getTplid() + "]不存在");
		}
		Workbook wb = null;
		// 创建工作簿
		wb = WorkbookFactory.create(new File(filePath));
		// 获取excel页
		Sheet sheet = wb.getSheetAt(0);
		// 获取总行数
		int rowCnt = sheet.getLastRowNum();
		
		List<TplDtl> tplDtlList=template.getDetail();
		int tplDtlSize=tplDtlList.size();
		String tplContent=template.getContent();
		String telVarName=template.getTelvarname();
		int startColNum=template.getStartcolnum()-1;
		String replaceContent="";
		for(int i=startColNum;i<=rowCnt;i++)
		{
			replaceContent=tplContent;
			Row row = sheet.getRow(i);
			SmsTaskDtl smsTaskDtl=new SmsTaskDtl();
			smsTaskDtl.setId(Utils.get16UUID());
			smsTaskDtl.setTaskid(taskInfo.getTaskid());
			Map<String,String> map=new HashMap<String,String>();
			for(int j=0;j<tplDtlSize;j++)
			{
				String varname=tplDtlList.get(j).getVarname();
				int colnum=Integer.valueOf(tplDtlList.get(j).getColnum());
				String cellVal;
				try
				{
					cellVal=Utils.getCellValue(row.getCell(colnum));
				}
				catch(Exception e)
				{
					throw new Exception("第"+(i+1)+"行的"+tplDtlList.get(j).getComment()+"异常");
				}
				if(varname.equals(telVarName))
				{
					if(cellVal.length()!=11)
						throw new Exception("第"+(i+1)+"行的"+tplDtlList.get(j).getComment()+"长度不是11位");
					smsTaskDtl.setTel(cellVal);
				}
				else
				{
					replaceContent=replaceContent.replaceAll("#\\{"+varname+"\\}", cellVal);
				}
			}
			smsTaskDtl.setContent(replaceContent);
			smsTaskDtl.setProcstatus("待处理");
			smsTaskDtl.setResendcnt(0);
			smsTaskDtlDao.insert(smsTaskDtl);
		}
		return rowCnt-startColNum+1;
	}
	/**
	 * 功能：任务进入待发送状态
	 * **/
	public void moveTaskToWaitSend(String taskid)
	{
		List<SmsTaskDtl> smsTaskDtlList=smsTaskDtlDao.selectListByTaskId(taskid,"待处理");
		int size=smsTaskDtlList.size();
		for(int i=0;i<size;i++)
		{
			SmsTaskDtl smsTaskDtl=smsTaskDtlList.get(i);
			SmsWaitSend smsWaitSend=new SmsWaitSend();
			smsWaitSend.setId(Utils.get16UUID());
			smsWaitSend.setSrcchnl("0");
			smsWaitSend.setDetailid(smsTaskDtl.getId());
			smsWaitSend.setContent(smsTaskDtl.getContent());
			smsWaitSend.setTel(smsTaskDtl.getTel());
			smsWaitSend.setProcstatus("待处理");
			smsWaitSendDao.insert(smsWaitSend);
			smsTaskDtl.setProcstatus("已处理");
			smsTaskDtlDao.updateByPrimaryKey(smsTaskDtl);
		}
		SmsTaskInfo smsTaskInfo=smsTaskInfoDao.selectByPrimaryKey(taskid);
		smsTaskInfo.setProcstatus("已处理");
		smsTaskInfoDao.updateByPrimaryKey(smsTaskInfo);
	}
	/**
	 * 功能：删除未确认的批量任务
	 * 参数：taskid:任务编号
	 * @throws Exception 
	 * **/
	public void deleteBatchSendMessageTask(String taskid) throws Exception
	{
		try
		{
			smsTaskDtlDao.deleteByTaskId(taskid);
			smsTaskInfoDao.deleteByPrimaryKey(taskid);
		}
		catch(Exception e)
		{
			throw new Exception(e.getMessage());
		}
		
	}
	/**
	 * 功能：根据任务号插叙任务明细
	 * 参数：taskid-任务号
	 * 返回：明细列表
	 * **/
	public List<SmsTaskDtl>  querySmsTaskDtlListByTaskId(String taskid,String procStatus)
	{
		return smsTaskDtlDao.selectListByTaskId(taskid,procStatus);
	}
	
	/**
	 * 功能：查询已发送明细
	 * 参数：content-短信内容
	 *     tel-短信号码
	 * 返回：符合条件已发送明细
	 * **/
	public List<SmsSended> querySmsSendedList(String content,String tel,Timestamp starttime,Timestamp endtime,String status)
	{
		Map params=new HashMap<String,Object>();
		params.put("content", content);
		params.put("tel", tel);
		params.put("starttime", starttime);
		params.put("endtime", endtime);
		params.put("status", status);
		return smsSendedDao.selectList(params);
	}
	
	
	/**
	 * 功能：检查定时发送短信任务
	 * 
	 * **/
	public void checkTimerSendSmsTask()
	{
		List<SmsTaskInfo> list=smsTaskInfoDao.selectTimerTaskList(new Timestamp(new Date().getTime()));
		for(int i=0;i<list.size();i++)
		{
			SmsTaskInfo task=list.get(i);
			this.moveTaskToWaitSend(task.getTaskid());
			task.setProcstatus("已处理");
			smsTaskInfoDao.updateByPrimaryKey(task);
		}
	}
	
	/**
	 * 功能：检查短信平台是否可用
	 * @throws Exception 
	 * **/
	public void checkSmsMerchantAvailable(int curBatchCnt) throws Exception
	{
		String result=smsMerchant.getBalance();
		Document document=DocumentHelper.parseText(result);
		Element root=document.getRootElement();
		String returnstatus=root.selectSingleNode("returnstatus").getText();
		String message=root.selectSingleNode("message").getText();
		String overage=root.selectSingleNode("overage").getText();
		if(returnstatus.equals("Faild"))
		{
			throw new Exception(message);
		}
		if(1<Integer.valueOf(overage))
		{
			throw new Exception("余额不足，当前短信剩余条数:"+overage+"条，本次发送条数："+curBatchCnt+"条");
		}
	}
	
	/**
	 * 功能：批量任务失败短信重发
	 * 参数：短信明细id编号
	 * @throws Exception 
	 * **/
	public void batchResendMsg(String ids[]) throws Exception
	{
		for(int i=0;i<ids.length;i++)
		{
			String id=ids[i];
			SmsTaskDtl item=smsTaskDtlDao.selectByPrimaryKey(id);
			if(item==null)
			{
				throw new Exception(id+"短信不存在");
			}
			if(!item.getProcstatus().trim().equals("发送失败"))
			{
				throw new Exception("短信发送状态不是发送失败，不允许重发");
			}
			if(item.getResendcnt()==3)
			{
				throw new Exception("重发次数已达三次，不允许多次发送");
			}
			
			SmsWaitSend smsWaitSend=new SmsWaitSend();
			smsWaitSend.setId(Utils.get16UUID());
			smsWaitSend.setSrcchnl("0");
			smsWaitSend.setDetailid(item.getId());
			smsWaitSend.setContent(item.getContent());
			smsWaitSend.setTel(item.getTel());
			smsWaitSend.setProcstatus("待处理");
			smsWaitSendDao.insert(smsWaitSend);
			item.setProcstatus("已处理");
			int resendCnt=item.getResendcnt()+1;
			item.setResendcnt(resendCnt);
			smsTaskDtlDao.updateByPrimaryKey(item);
		}
		
	}
	/**
	 * 功能：发送单条短信
	 * 参数：msg-短信内容
	 *     telno-短信号码
	 * @throws Exception 
	 * **/
	 public void sendOne2OneMsg(String msg,String telno) throws Exception
	 {
		 try
		 {
			if(telno.trim().length()!=11)
			{
				throw new Exception("电话号码长度不是11位");
			}
			if(StringUtils.isNumeric(telno)==false)
			{
				throw new Exception("电话号码存在非数字字符，请检查 ");
			}
			SmsWaitSend smsWaitSend = new SmsWaitSend();
			smsWaitSend.setId(Utils.get16UUID());
			smsWaitSend.setSrcchnl("1");
			smsWaitSend.setDetailid("");
			smsWaitSend.setContent(msg);
			smsWaitSend.setTel(telno);
			smsWaitSend.setProcstatus("待处理");
			smsWaitSendDao.insert(smsWaitSend);
		 }catch(Exception e)
		 {
			 throw new Exception(e.getMessage());
		 }
		
	 }
}
