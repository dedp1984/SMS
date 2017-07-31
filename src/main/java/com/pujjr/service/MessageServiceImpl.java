package com.pujjr.service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pujjr.custom.Utils;
import com.pujjr.dao.SmsWaitSendMapper;
import com.pujjr.domain.SmsWaitSend;
import com.pujjr.merchant.SmsMerchant;
import com.pujjr.message_api.service.IMessageService;
@Service
public class MessageServiceImpl implements IMessageService {

	private static Logger log = Logger.getLogger(MessageServiceImpl.class); 
	
	@Autowired
	private SmsWaitSendMapper smsWaitSendDao;
	@Resource
	private SmsMerchant smsMerchant;
	//供应商名称
	private String supplyName;
	
	public String getSupplyName() {
		return supplyName;
	}
	public void setSupplyName(String supplyName) {
		this.supplyName = supplyName;
	}
	@Override
	public String sendSms(String sendId, String mobile, String content) throws Exception {
		// TODO Auto-generated method stub
		log.info("----------------------------------------------开始调用短信发送接口-----------------------------------------");
		try
		 {
			if(mobile.trim().length()!=11)
			{
				throw new Exception("电话号码长度不是11位");
			}
			if(StringUtils.isNumeric(mobile)==false)
			{
				throw new Exception("电话号码存在非数字字符，请检查 ");
			}
			log.info("mobile = "+mobile+",content="+content);
			SmsWaitSend smsWaitSend = new SmsWaitSend();
			smsWaitSend.setId(Utils.get16UUID());
			smsWaitSend.setSrcchnl("2");
			smsWaitSend.setDetailid(sendId);
			smsWaitSend.setContent(content);
			smsWaitSend.setTel(mobile);
			smsWaitSend.setProcstatus("待处理");
			smsWaitSendDao.insert(smsWaitSend);
		 }catch(Exception e)
		 {
			 throw new Exception(e.getMessage());
		 }
		log.info("----------------------------------------------结束调用短信发送接口-----------------------------------------");
		return supplyName;
	}

}
