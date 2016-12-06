package com.pujjr.custom;

import java.util.HashMap;
import java.util.Map;

public class Result 
{
	/**
	 * ������
	 * true-�ɹ�
	 * false-ʧ��
	 * **/
	private boolean success;
	/**
	 * ������Ϣ
	 * Ĭ��ֵerrmsg-������ʾ��Ϣ
	 * Ĭ��ֵerrcode-�������
	 * **/
	private Map<String, Object> errors = new HashMap<String, Object>();
	/**
	 * ��ҳ��ѯ��ǰҳ��¼����
	 * **/
	private String totalsize;
	/**
	 * ��ҳ��ѯ��ǰҳ��¼��Ϣ 
	 * **/
	private Object items;
	
	public Result()
	{
		
	}
	
	public Result(boolean flag)
	{
		this.success=flag;
	}
	public Result(boolean flag,String errmsg)
	{
		this.success=flag;
		this.errors.put("errmsg", errmsg);
	}
	
	public Object getItems() {
		return items;
	}
	public void setItems(Object items) {
		this.items = items;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public Map<String, Object> getErrors() {
		return errors;
	}
	public void setErrors(Map<String, Object> errors) {
		this.errors = errors;
	}
	public String getTotalsize() {
		return totalsize;
	}
	public void setTotalsize(String totalsize) {
		this.totalsize = totalsize;
	}
	
	public void addErros(String key,Object object)
	{
		errors.put(key, object);
	}
}
