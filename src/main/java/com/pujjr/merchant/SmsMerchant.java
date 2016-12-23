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
	// ��ҵID
	private String userid;	
	// ��������ַ
	private String serverUrl;	
	// �û��ʺţ���ϵͳ����Ա
	private String account;
	// �û��˺Ŷ�Ӧ������
	private String password;
	//��ѯ״̬URL 
	private String serverUrlStatus;
	//��ѯ���нӿ�URL
	private String serverUrlCallApi;
	//��Ӧ������
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
	 * ��ѯ����뷢����
	 * @return ���󷵻�ֵ
	 * @throws Exception
	 */
	public String getBalance() throws Exception {

		// ������ֵ
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("account", account));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("action", "overage"));
		// �ύ����
		String result = HttpUtil.request(serverUrl, params);
		System.out.println("balance result="+result);
		return result;
	}
	/**
	 * �Ƿ��ؼ��ʼ��
	 * @param content ���������
	 * @return ���ؽ��
	 * @throws Exception
	 */
	public String checkContent(String content) throws Exception {

		// ������ֵ
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("account", account));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("action", "checkkeyword"));
		params.add(new BasicNameValuePair("content", content));

		// �ύ����
		String result = HttpUtil.request(serverUrl, params);
		return result;
	}
	
	/**
	 * ��ȡ���ر�������
	 * @return ���ر�������
	 * @throws Exception
	 */
	public String getReport() throws Exception {
		
		// ������ֵ
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("account", account));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("action", "query"));

		// �ύ����
		String result = HttpUtil.request(serverUrlStatus, params);
		return result;
	}
	
	/**
	 * ��ȡ��������
	 * @return ��ȡ��������
	 * @throws Exception
	 */
	public String getMo() throws Exception {
		
		// ������ֵ
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("account", account));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("action", "query"));

		// �ύ����
		String result = HttpUtil.request(serverUrlCallApi, params);
		return result;
	}
	
	/**
	 * ���Ͷ��ţ�utf-8��ʽ���ݣ�
	 * 
	 * @param mobile
	 *            �ֻ��ţ����ʹ�ð�Ƕ��ŷָ�
	 * @param content
	 *            ����
	 * @param sendTime
	 *            ��ʱʱ�䣬��ʽ2010-10-24 09:08:10��С�ڵ�ǰʱ���Ϊ�ձ�ʾ��������
	 * @param extno
	 *            ��չ��
	 * @return ���ͷ���ֵ
	 * @throws Exception
	 *             �׳��쳣
	 */
	public String sendSms(String mobile, String content, String sendTime,
			String extno) throws Exception {

		// ������ֵ
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("account", account));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("mobile", mobile));
		params.add(new BasicNameValuePair("content", content));
		params.add(new BasicNameValuePair("sendTime", sendTime));
		params.add(new BasicNameValuePair("action", "send"));
		params.add(new BasicNameValuePair("extno", extno));

		// �ύ����
		String result = HttpUtil.request(serverUrl, params);
		return result;
	}
	
		
}
