package com.pujjr.merchant;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Scope("singleton")
public class SmsMerchant
{
	// 企业ID
	private String userid;	
	// 服务器地址
	private String serverUrl;	
	// 用户帐号，由系统管理员
	private String account;
	// 用户账号对应的密码
	private String password;
	//查询状态URL 
	private String serverUrlStatus;
	//查询上行接口URL
	private String serverUrlCallApi;
	//供应商名称
	private String supplyName;
	
	public String getSupplyName() {
		return supplyName;
	}
	public void setSupplyName(String supplyName) {
		this.supplyName = supplyName;
	}
	public String getServerUrlCallApi()
	{
		return serverUrlCallApi;
	}
	public void setServerUrlCallApi(String serverUrlCallApi)
	{
		this.serverUrlCallApi = serverUrlCallApi;
	}
	public String getUserid()
	{
		return userid;
	}
	public void setUserid(String userid)
	{
		this.userid = userid;
	}
	public String getServerUrl()
	{
		return serverUrl;
	}
	public void setServerUrl(String serverUrl)
	{
		this.serverUrl = serverUrl;
	}
	public String getAccount()
	{
		return account;
	}
	public void setAccount(String account)
	{
		this.account = account;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	public String getServerUrlStatus()
	{
		return serverUrlStatus;
	}
	public void setServerUrlStatus(String serverUrlStatus)
	{
		this.serverUrlStatus = serverUrlStatus;
	}
	
	
	/**
	 * 查询余额与发送量
	 * @return 请求返回值
	 * @throws Exception
	 */
	public String getBalance() throws Exception {

		// 参数赋值
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("account", account));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("action", "overage"));
		// 提交请求
		String result = HttpUtil.request(serverUrl, params);
		System.out.println("balance result="+result);
		return result;
	}
	/**
	 * 非法关键词检查
	 * @param content 待检查内容
	 * @return 返回结果
	 * @throws Exception
	 */
	public String checkContent(String content) throws Exception {

		// 参数赋值
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("account", account));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("action", "checkkeyword"));
		params.add(new BasicNameValuePair("content", content));

		// 提交请求
		String result = HttpUtil.request(serverUrl, params);
		return result;
	}
	
	/**
	 * 获取返回报告数据
	 * @return 返回报告数据
	 * @throws Exception
	 */
	public String getReport() throws Exception {
		
		// 参数赋值
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("account", account));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("action", "query"));

		// 提交请求
		String result = HttpUtil.request(serverUrlStatus, params);
		return result;
	}
	
	/**
	 * 获取上行数据
	 * @return 获取上行数据
	 * @throws Exception
	 */
	public String getMo() throws Exception {
		
		// 参数赋值
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("account", account));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("action", "query"));

		// 提交请求
		String result = HttpUtil.request(serverUrlCallApi, params);
		return result;
	}
	
	/**
	 * 发送短信（utf-8格式内容）
	 * 
	 * @param mobile
	 *            手机号，多个使用半角逗号分隔
	 * @param content
	 *            内容
	 * @param sendTime
	 *            定时时间，格式2010-10-24 09:08:10，小于当前时间或为空表示立即发送
	 * @param extno
	 *            扩展码
	 * @return 发送返回值
	 * @throws Exception
	 *             抛出异常
	 */
	public String sendSms(String mobile, String content, String sendTime,
			String extno) throws Exception {

		// 参数赋值
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("account", account));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("mobile", mobile));
		params.add(new BasicNameValuePair("content", content));
		params.add(new BasicNameValuePair("sendTime", sendTime));
		params.add(new BasicNameValuePair("action", "send"));
		params.add(new BasicNameValuePair("extno", extno));

		// 提交请求
		String result = HttpUtil.request(serverUrl, params);
		return result;
	}
	
		
}
