package com.pujjr.service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pujjr.custom.Utils;
import com.pujjr.dao.SmsWaitSendMapper;
import com.pujjr.domain.SmsWaitSend;
import com.pujjr.merchant.SmsMerchant;
import com.pujjr.message_api.service.IMessageService;
@Service
public class MessageServiceImpl implements IMessageService {

	@Autowired
	private SmsWaitSendMapper smsWaitSendDao;
	@Resource
	private SmsMerchant smsMerchant;
	//��Ӧ������
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
		System.out.println("sendIds");
		try
		 {
			if(mobile.trim().length()!=11)
			{
				throw new Exception("�绰���볤�Ȳ���11λ");
			}
			if(StringUtils.isNumeric(mobile)==false)
			{
				throw new Exception("�绰������ڷ������ַ������� ");
			}
			SmsWaitSend smsWaitSend = new SmsWaitSend();
			smsWaitSend.setId(Utils.get16UUID());
			smsWaitSend.setSrcchnl("2");
			smsWaitSend.setDetailid(sendId);
			smsWaitSend.setContent(content);
			smsWaitSend.setTel(mobile);
			smsWaitSend.setProcstatus("������");
			smsWaitSendDao.insert(smsWaitSend);
		 }catch(Exception e)
		 {
			 throw new Exception(e.getMessage());
		 }
		return supplyName;
	}

}
