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
	 * 功能：查询经销商列表
	 * 参数：name-经销商名称
	 * 返回：经销商列表
	 * **/
	public List<JXS> queryJXSList(String name)
	{
		return JXSDao.selectList(name);
	}
	
	 /**
	  * 功能：增加经销商
	  * 参数：jxs-经销商信息
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
	 * 功能：更新经销商信息
	 * 参数：jxs-经销商信息
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
	 * 功能：删除经销商
	 * 参数：id-经销商ID
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
	 * 功能：根据经销商ID查询经销商信息
	 * 参数：ID-经销商ID
	 * **/
	public JXS queryJXSById(String id)
	{
		return JXSDao.selectByPrimaryKey(id);
	}
}
