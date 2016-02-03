package com.pujjr.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pujjr.dao.SmsSendedMapper;
import com.pujjr.dao.SmsTaskDtlMapper;
import com.pujjr.dao.SmsWaitSendMapper;
import com.pujjr.domain.SmsSended;
import com.pujjr.domain.SmsTaskDtl;
import com.pujjr.domain.SmsWaitSend;
import com.pujjr.merchant.SmsMerchant;

public class SmsThreadService
{
	public static Logger log4j=Logger.getLogger(SmsThreadService.class.getName());
	
	private static ArrayBlockingQueue queue;
	
	@Resource
	private SmsWaitSendMapper smsWaitSendDao;
	@Resource
	private SmsSendedMapper smsSendedDao;
	@Resource
	private SmsTaskDtlMapper smsTaskDtlDao;
	@Resource
	private SmsMerchant smsMerchant;
	
	private ExecutorService executor;
	
	private int maxQueueSize;
	private int maxThreadPoolSize;
	private int maxReadThreadSize;
	private int maxWriteThreadSize;
	
	public int getMaxQueueSize()
	{
		return maxQueueSize;
	}
	public void setMaxQueueSize(int maxQueueSize)
	{
		this.maxQueueSize = maxQueueSize;
	}
	public int getMaxThreadPoolSize()
	{
		return maxThreadPoolSize;
	}
	public void setMaxThreadPoolSize(int maxThreadPoolSize)
	{
		this.maxThreadPoolSize = maxThreadPoolSize;
	}
	public int getMaxReadThreadSize()
	{
		return maxReadThreadSize;
	}
	public void setMaxReadThreadSize(int maxReadThreadSize)
	{
		this.maxReadThreadSize = maxReadThreadSize;
	}
	public int getMaxWriteThreadSize()
	{
		return maxWriteThreadSize;
	}
	public void setMaxWriteThreadSize(int maxWriteThreadSize)
	{
		this.maxWriteThreadSize = maxWriteThreadSize;
	}
	@PostConstruct
	private void startService()
	{
		log4j.info("start Service");
		queue=new ArrayBlockingQueue<SmsWaitSend>(maxQueueSize);
		executor = Executors.newFixedThreadPool(maxThreadPoolSize);
		for(int i=0;i<maxWriteThreadSize;i++)
		{
			WriteSmsQueueThread writer=new WriteSmsQueueThread(queue,"WriteSmsQueueThread-"+i);
			executor.execute(writer);
		}
		
		for(int i=0;i<maxReadThreadSize;i++)
		{
			ReadSmsQueueThread reader=new ReadSmsQueueThread(queue,"ReadSmsQueueThread-"+i);
			executor.execute(reader);
		}
		
		SmsQueryStatusThread queryStatus=new SmsQueryStatusThread();
		executor.execute(queryStatus);
		
	}
	@PreDestroy
	private void stopService()
	{
		log4j.info("stop Service");
		executor.shutdownNow();
	}
	private class WriteSmsQueueThread implements Runnable
	{
		private BlockingQueue queue;
		private String threadName;
		private int i=0;
		public WriteSmsQueueThread(ArrayBlockingQueue queue,String threadName)
		{
			this.queue=queue;
			this.threadName=threadName;
		}
		@Override
		public void run()
		{
			// TODO Auto-generated method stub
			try
			{
				log4j.info("线程"+threadName+"开始扫描待发送表。。。。。");
				while(!Thread.currentThread().isInterrupted())
				{
					i++;
					List<SmsWaitSend> list=smsWaitSendDao.selectList("待处理");
					for(int i=0;i<list.size();i++)
					{
						SmsWaitSend item=list.get(i);
						queue.add(item);
						smsWaitSendDao.deleteByPrimaryKey(item.getId());
					}
					Thread.sleep(10*1000);
					
				}
				log4j.info("线程"+threadName+"结束扫描待发送表。。。。。");
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}
	}
	private class ReadSmsQueueThread implements Runnable
	{
		private BlockingQueue queue;
		private String threadname;
		public ReadSmsQueueThread(ArrayBlockingQueue queue,String name)
		{
			this.queue=queue;
			this.threadname=name;
		}
		@Override
		public void run()
		{
			// TODO Auto-generated method stub
			try
			{
				log4j.info("线程"+threadname+"开始监听短信发送队列");
				while(!Thread.currentThread().isInterrupted())
				{
						SmsWaitSend item=(SmsWaitSend)queue.take();
						System.out.println("thread "+threadname+" "+item.getContent());
						String procStatus="已发送";
						String result=smsMerchant.sendSms(item.getTel(), item.getContent(), "", "");
						Document document=DocumentHelper.parseText(result);
						Element root=document.getRootElement();
						String returnstatus=root.selectSingleNode("returnstatus").getText();
						String message=root.selectSingleNode("message").getText();
						String taskId=root.selectSingleNode("taskID").getText();
						if(!returnstatus.equals("Success"))
						{
							procStatus="发送失败:"+message;
						}
						SmsSended smsSended=new SmsSended();
						smsSended.setId(item.getId());
						smsSended.setSrcchnl(item.getSrcchnl());
						smsSended.setDetailid(item.getDetailid());
						smsSended.setContent(item.getContent());
						smsSended.setTel(item.getTel());
						smsSended.setProcstatus(procStatus);
						smsSended.setSendtaskid(taskId);
						smsSended.setSendtime(new Timestamp(new Date().getTime()));
						smsSendedDao.insert(smsSended);
						//如果来自渠道0网页发送，则需要更新批量任务明细信息
						if(item.getSrcchnl().equals("0"))
						{
							String detailId=item.getDetailid();
							SmsTaskDtl detail=smsTaskDtlDao.selectByPrimaryKey(detailId);
							detail.setProcstatus(procStatus);
							smsTaskDtlDao.updateByPrimaryKey(detail);
						}
				}
				log4j.info("线程"+threadname+"结束监听短信发送队列");
			}catch(Exception e )
			{
				e.printStackTrace();
			}
			
		}
	}
	private class SmsQueryStatusThread implements Runnable
	{

		@Override
		public void run()
		{
			// TODO Auto-generated method stub
			try
			{
				log4j.info("启动短信发送状态查询检查线程 ");
				while(!Thread.currentThread().isInterrupted())
				{
						String result=smsMerchant.getReport();
						log4j.info("status="+result);
						Document document=DocumentHelper.parseText(result);
						Element root=document.getRootElement();
						Element errors=root.element("errorstatus");
						if(errors==null)
						{
							List<Node> list=root.selectNodes("statusbox");
							for(int i=0;i<list.size();i++)
							{
								Node node=list.get(i);
								String status=node.selectSingleNode("status").getText();
								String mobile=node.selectSingleNode("mobile").getText();
								String taskid=node.selectSingleNode("taskid").getText();

								SmsSended record=smsSendedDao.selectBySendTaskId(taskid, mobile);
								String procStatus;
								if(status.equals("10"))
								{
									procStatus="发送成功";
								}
								else
								{
									procStatus="发送失败";
								}
								record.setProcstatus(procStatus);
								smsSendedDao.updateBySendTaskId(record);
								//如果来自网页批量渠道则需更新对应的批量明细记录
								if(record.getSrcchnl().equals("0"))
								{
									 SmsTaskDtl dtl=smsTaskDtlDao.selectByPrimaryKey(record.getDetailid());
									 dtl.setProcstatus(procStatus);
									 smsTaskDtlDao.updateByPrimaryKey(dtl);
								}
							}
							
						}
						Thread.sleep(60000);
					
				}
				log4j.info("结束短信发送状态查询检查线程 ");
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			
			
		}
		
	}
	
}
