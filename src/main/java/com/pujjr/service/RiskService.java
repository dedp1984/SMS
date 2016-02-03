package com.pujjr.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.pujjr.dao.JXSMapper;
import com.pujjr.domain.JXS;

@Service
public class RiskService
{
	@Resource
	private JXSMapper JXSDao;
	/**
	 * ���ܣ���ѯ�������б�
	 * ������name-����������
	 * ���أ��������б�
	 * **/
	public List<JXS> queryJXSList(String name)
	{
		return JXSDao.selectList(name);
	}
	
	 /**
	  * ���ܣ����Ӿ�����
	  * ������jxs-��������Ϣ
	  * 
	  * **/
	public void addJXS(JXS jxs) throws Exception
	{
		try
		{
			JXSDao.insert(jxs);
		}
		catch(Exception e)
		{
			throw new Exception(e.getMessage());
		}
	}
	/**
	 * ���ܣ����¾�������Ϣ
	 * ������jxs-��������Ϣ
	 * 
	 * **/
	public void updateJXS(JXS jxs) throws Exception
	{
		try
		{
			JXSDao.updateByPrimaryKey(jxs);
		}catch(Exception e)
		{
			throw new Exception(e.getMessage());
		}
	}
	
	/**
	 * ���ܣ�ɾ��������
	 * ������id-������ID
	 * @throws Exception 
	 * **/
	public void deleteJXS(String id) throws Exception
	{
		try
		{
			JXSDao.deleteByPrimaryKey(id);
		}
		catch(Exception e)
		{
			throw new Exception(e.getMessage());
		}
	}
	
	/***
	 * ���ܣ����ݾ�����ID��ѯ��������Ϣ
	 * ������ID-������ID
	 * **/
	public JXS queryJXSById(String id)
	{
		return JXSDao.selectByPrimaryKey(id);
	}
}
