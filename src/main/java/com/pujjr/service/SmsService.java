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
	 * ����ID��ѯģ����Ϣ
	 * **/
	public Template queryTemplateById(String id)
	{
		return templateDao.selectByPrimaryKey(id);
	}
	/**
	 * ���ܣ���ѯ����ģ���б�
	 * ������name-ģ������
	 * ���أ�ģ���б���Ϣ
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
				throw new Exception("ģ�������б�������ȱʧ������");
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
				throw new Exception("����"+varname+"δ����");
			}
			varcnt++;
			startIndex=endIndex+1;
		}
		//����Ҫ�ѽ��պ���Ҳ���ȥ
		if((varcnt+1)!=detailList.size())
		{
			throw new Exception("ģ����������붨��ı�����������");
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
			throw new Exception("����"+template.getTelvarname()+"δ����");
		}
	}
	 /**
	  * ���ܣ����ģ��
	  * ������template-ģ����Ϣ
	  *     detaiList-ģ����ϸ��Ϣ
	  * ���أ���
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
	 * ���ܣ�ɾ������ģ��
	 * ������id-ģ����
	 * ���أ���
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
	 * ���ܣ����¶���ģ����Ϣ
	 * ������template-ģ����Ϣ
	 *     detailList-ģ����ϸ��Ϣ
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
	 * ���ܣ���ѯ�������������б�
	 * ������ taskname-��������
	 *     createdate-���񴴽�����
	 * ���أ����������б�
	 * **/
	public List<SmsTaskInfo>  queryTaskList(String taskname,Timestamp starttime,Timestamp endtime,String createid)
	{
		return smsTaskInfoDao.selectList(taskname, starttime,endtime,createid);
	}
	
	/**
	 * ���ܣ�����ģ���������Ͷ���
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
		//����������Ϣ
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
				taskInfo.setProcstatus("������");
			else
				taskInfo.setProcstatus("��ȷ��");
			smsTaskInfoDao.updateByPrimaryKey(taskInfo);
			
			
		}catch(Exception e)
		{
			throw new Exception(e.getMessage());
		}
		return taskId;
		
	}
	/**
	 * ���ܣ������ļ�����������ϸ��Ϣ
	 * ������filePath-�ļ�·��
	 *     taskInfo-������Ϣ
	 * ���أ������ܱ���
	 * **/
	private int generateTaskDetail(String filePath,SmsTaskInfo taskInfo) throws Exception
	{
		//��ȡģ����Ϣ
		Template template = templateDao.selectByPrimaryKey(taskInfo.getTplid());
		if (template == null)
		{
			throw new Exception("����ģ��id[" + taskInfo.getTplid() + "]������");
		}
		Workbook wb = null;
		// ����������
		wb = WorkbookFactory.create(new File(filePath));
		// ��ȡexcelҳ
		Sheet sheet = wb.getSheetAt(0);
		// ��ȡ������
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
					throw new Exception("��"+(i+1)+"�е�"+tplDtlList.get(j).getComment()+"�쳣");
				}
				if(varname.equals(telVarName))
				{
					if(cellVal.length()!=11)
						throw new Exception("��"+(i+1)+"�е�"+tplDtlList.get(j).getComment()+"���Ȳ���11λ");
					smsTaskDtl.setTel(cellVal);
				}
				else
				{
					replaceContent=replaceContent.replaceAll("#\\{"+varname+"\\}", cellVal);
				}
			}
			smsTaskDtl.setContent(replaceContent);
			smsTaskDtl.setProcstatus("������");
			smsTaskDtl.setResendcnt(0);
			smsTaskDtlDao.insert(smsTaskDtl);
		}
		return rowCnt-startColNum+1;
	}
	/**
	 * ���ܣ�������������״̬
	 * **/
	public void moveTaskToWaitSend(String taskid)
	{
		List<SmsTaskDtl> smsTaskDtlList=smsTaskDtlDao.selectListByTaskId(taskid,"������");
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
			smsWaitSend.setProcstatus("������");
			smsWaitSendDao.insert(smsWaitSend);
			smsTaskDtl.setProcstatus("�Ѵ���");
			smsTaskDtlDao.updateByPrimaryKey(smsTaskDtl);
		}
		SmsTaskInfo smsTaskInfo=smsTaskInfoDao.selectByPrimaryKey(taskid);
		smsTaskInfo.setProcstatus("�Ѵ���");
		smsTaskInfoDao.updateByPrimaryKey(smsTaskInfo);
	}
	/**
	 * ���ܣ�ɾ��δȷ�ϵ���������
	 * ������taskid:������
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
	 * ���ܣ���������Ų���������ϸ
	 * ������taskid-�����
	 * ���أ���ϸ�б�
	 * **/
	public List<SmsTaskDtl>  querySmsTaskDtlListByTaskId(String taskid,String procStatus)
	{
		return smsTaskDtlDao.selectListByTaskId(taskid,procStatus);
	}
	
	/**
	 * ���ܣ���ѯ�ѷ�����ϸ
	 * ������content-��������
	 *     tel-���ź���
	 * ���أ����������ѷ�����ϸ
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
	 * ���ܣ���鶨ʱ���Ͷ�������
	 * 
	 * **/
	public void checkTimerSendSmsTask()
	{
		List<SmsTaskInfo> list=smsTaskInfoDao.selectTimerTaskList(new Timestamp(new Date().getTime()));
		for(int i=0;i<list.size();i++)
		{
			SmsTaskInfo task=list.get(i);
			this.moveTaskToWaitSend(task.getTaskid());
			task.setProcstatus("�Ѵ���");
			smsTaskInfoDao.updateByPrimaryKey(task);
		}
	}
	
	/**
	 * ���ܣ�������ƽ̨�Ƿ����
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
			throw new Exception("���㣬��ǰ����ʣ������:"+overage+"�������η���������"+curBatchCnt+"��");
		}
	}
	
	/**
	 * ���ܣ���������ʧ�ܶ����ط�
	 * ������������ϸid���
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
				throw new Exception(id+"���Ų�����");
			}
			if(!item.getProcstatus().trim().equals("����ʧ��"))
			{
				throw new Exception("���ŷ���״̬���Ƿ���ʧ�ܣ��������ط�");
			}
			if(item.getResendcnt()==3)
			{
				throw new Exception("�ط������Ѵ����Σ��������η���");
			}
			
			SmsWaitSend smsWaitSend=new SmsWaitSend();
			smsWaitSend.setId(Utils.get16UUID());
			smsWaitSend.setSrcchnl("0");
			smsWaitSend.setDetailid(item.getId());
			smsWaitSend.setContent(item.getContent());
			smsWaitSend.setTel(item.getTel());
			smsWaitSend.setProcstatus("������");
			smsWaitSendDao.insert(smsWaitSend);
			item.setProcstatus("�Ѵ���");
			int resendCnt=item.getResendcnt()+1;
			item.setResendcnt(resendCnt);
			smsTaskDtlDao.updateByPrimaryKey(item);
		}
		
	}
	/**
	 * ���ܣ����͵�������
	 * ������msg-��������
	 *     telno-���ź���
	 * @throws Exception 
	 * **/
	 public void sendOne2OneMsg(String msg,String telno) throws Exception
	 {
		 try
		 {
			if(telno.trim().length()!=11)
			{
				throw new Exception("�绰���볤�Ȳ���11λ");
			}
			if(StringUtils.isNumeric(telno)==false)
			{
				throw new Exception("�绰������ڷ������ַ������� ");
			}
			SmsWaitSend smsWaitSend = new SmsWaitSend();
			smsWaitSend.setId(Utils.get16UUID());
			smsWaitSend.setSrcchnl("1");
			smsWaitSend.setDetailid("");
			smsWaitSend.setContent(msg);
			smsWaitSend.setTel(telno);
			smsWaitSend.setProcstatus("������");
			smsWaitSendDao.insert(smsWaitSend);
		 }catch(Exception e)
		 {
			 throw new Exception(e.getMessage());
		 }
		
	 }
}
