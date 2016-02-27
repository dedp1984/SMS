package com.pujjr.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pujjr.dao.SysAccountFeatureMapper;
import com.pujjr.dao.SysAccountMapper;
import com.pujjr.dao.SysAccountRoleMapper;
import com.pujjr.domain.SysAccount;
import com.pujjr.domain.SysAccountFeatureKey;
import com.pujjr.domain.SysAccountRoleKey;

@Service
@Transactional(rollbackFor=Exception.class)
public class SysAccountService 
{
	@Resource
	private SysAccountMapper sysAccountDao;
	@Resource
	private SysAccountRoleMapper sysAccountRoleDao;
	@Resource
	private SysAccountFeatureMapper sysAccountFeatureDao;
	
	public SysAccount querySysAccountByAccountId(String accountid)
	{
		return sysAccountDao.selectByPrimaryKey(accountid);
	}
	
	public List<SysAccount> querySysAccountList(String accountid,String accountname,String branchid,ArrayList<String> propertyList)
	{
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("accountid", accountid);
		map.put("accountname", accountname);
		map.put("branchid", branchid);
		map.put("propertyList", propertyList);
		List<SysAccount> list= sysAccountDao.selectAccountList(map);
		for(int i=0;i<list.size();i++)
		{
			List<SysAccountRoleKey> list1=list.get(i).getRoles();
			for(int j=0;j<list1.size();j++)
			{
				SysAccountRoleKey accountRole=list1.get(j);
				System.out.println("account roleid="+accountRole.getRoleid());
			}
			
		}
		return list;
	}
	public String generateEncryptPasswd(String passwd) throws UnsupportedEncodingException, NoSuchAlgorithmException
	{
		MessageDigest md5=MessageDigest.getInstance("MD5");
		MessageDigest sha256=MessageDigest.getInstance("SHA-256");
		//生产64位用户随机盐
		Random ranGen = new SecureRandom();
        byte[] bSlot = new byte[32];
        String sSlot="";
        ranGen.nextBytes(bSlot);
        for(byte b:bSlot)
        {
        	sSlot+=String.format("%02X", b);
        }
        System.out.println("slot="+sSlot);
        md5.update(bSlot);
		byte[] bMd5Slot=md5.digest();
		String sMd5Slot="";
		for(byte b:bMd5Slot)
		{
			sMd5Slot+=String.format("%02X", b);
		}
		System.out.println("md5 slot="+sMd5Slot);
		sha256.update((passwd+sMd5Slot).getBytes("GBK"));
		byte[] bSha256=sha256.digest();
		String sSha256="";
		for(byte b:bSha256)
		{
			sSha256+=String.format("%02X", b);
		} 
		System.out.println("sha256="+sSha256);
		md5.update(bSha256);
		byte[] bMd5Sha256=md5.digest();
		String sMd5Sha256="";
		for(byte b:bMd5Sha256)
		{
			sMd5Sha256+=String.format("%02X", b);	
		}
		System.out.println("md5 sha256="+sMd5Sha256);
		
		String encryptPasswd="";
		for(int i=0;i<sMd5Sha256.length();i++)
		{
			encryptPasswd+=sMd5Sha256.substring(i, i+1)+sMd5Slot.substring(i, i+1);
		}
		return encryptPasswd;
		
	}
	
	public boolean verifyPasswd(String passwd,String encryptPasswd) throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		MessageDigest md5=MessageDigest.getInstance("MD5");
		MessageDigest sha256=MessageDigest.getInstance("SHA-256");
		String sMd5Slot="";
		String passwdHash="";
		for(int i=0;i<encryptPasswd.length();i=i+2)
		{
			passwdHash+=encryptPasswd.substring(i, i+1);
			sMd5Slot+=encryptPasswd.substring(i+1, i+2);
		}
		sha256.update((passwd+sMd5Slot).getBytes("GBK"));
		byte[] bSha256=sha256.digest();
		String sSha256="";
		for(byte b:bSha256)
		{
			sSha256+=String.format("%02X", b);
		} 
		System.out.println("sha256="+sSha256);
		md5.update(bSha256);
		byte[] bMd5Sha256=md5.digest();
		String sMd5Sha256="";
		for(byte b:bMd5Sha256)
		{
			sMd5Sha256+=String.format("%02X", b);	
		}
		if(sMd5Sha256.equals(passwdHash))
		{
			return true;
		}
		else
		{
			return false;
		}
		
	}
	public int addSysAccount(SysAccount sysAccount,String rolelist) throws UnsupportedEncodingException, NoSuchAlgorithmException
	{
		sysAccount.setPassword(this.generateEncryptPasswd("111111"));
		sysAccountDao.insert(sysAccount);
		List<SysAccountFeatureKey> busiList=sysAccount.busiFeature;
		for(int j=0;j<busiList.size();j++)
		{
			SysAccountFeatureKey feature=busiList.get(j);
			sysAccountFeatureDao.insert(feature);
		}
		if(rolelist.length()!=0)
		{
			String[] arrRole=rolelist.split(",");
			for(int i=0;i<arrRole.length;i++)
			{
				SysAccountRoleKey sysAccountRoleKey=new SysAccountRoleKey();
				sysAccountRoleKey.setAccountid(sysAccount.getAccountid());
				sysAccountRoleKey.setRoleid(arrRole[i]);
				if(sysAccountRoleDao.insert(sysAccountRoleKey)==0)
				{
					return 0;
				}
			}

		}
		return 1;
	}
	
	public int modifySysAccount(SysAccount sysAccount,String rolelist)
	{
		sysAccountRoleDao.deleteByAccountid(sysAccount.getAccountid());
		if(sysAccountDao.updateByPrimaryKey(sysAccount)==0)
		{
			return 0;
		}
		SysAccountFeatureKey key=new SysAccountFeatureKey();
		key.setAccountid(sysAccount.getAccountid());
		key.setType("1");
		sysAccountFeatureDao.deleteBySelective(key);
		List<SysAccountFeatureKey> busiList=sysAccount.busiFeature;
		for(int j=0;j<busiList.size();j++)
		{
			SysAccountFeatureKey feature=busiList.get(j);
			sysAccountFeatureDao.insert(feature);
		}
		if(rolelist.length()!=0)
		{
			String[] arrRole=rolelist.split(",");
			for(int i=0;i<arrRole.length;i++)
			{
				SysAccountRoleKey sysAccountRoleKey=new SysAccountRoleKey();
				sysAccountRoleKey.setAccountid(sysAccount.getAccountid());
				sysAccountRoleKey.setRoleid(arrRole[i]);
				if(sysAccountRoleDao.insert(sysAccountRoleKey)==0)
				{
					return 0;
				}
			}

		}
		return 1;
	}
	
	public int modifySysAccount(SysAccount sysAccount)
	{
		if(sysAccountDao.updateByPrimaryKey(sysAccount)==0)
		{
			return 0;
		}
		return 1;
	}
	
	public void deleteSysAccount(String accountid) throws Exception
	{
		sysAccountRoleDao.deleteByAccountid(accountid);
		SysAccountFeatureKey key=new SysAccountFeatureKey();
		key.setAccountid(accountid);
		sysAccountFeatureDao.deleteBySelective(key);
		sysAccountDao.deleteByPrimaryKey(accountid);
	}
	
	public List<SysAccount> querySysAccountListByBranchId(String branchid)
	{
		return sysAccountDao.selectAccountListByBranchId(branchid);
	}
	
	public List<SysAccountFeatureKey> querySysAccountFeature(String accountid,String type,String value)
	{
		SysAccountFeatureKey key =new SysAccountFeatureKey();
		key.setAccountid(accountid);
		key.setType(type);
		key.setValue(value);
		return sysAccountFeatureDao.selectBySelective(key);
	}
	
	public void batchModifySysAccountBranch(String oldBranchId,String newBranchId,String modifyUsers)
	{
		String[] users=modifyUsers.split("\\,");
		for(int i=0;i<users.length;i++)
		{
			String accountid=users[i];
			SysAccount sysAccount=sysAccountDao.selectByPrimaryKey(accountid);
			if(sysAccount!=null)
			{
				sysAccount.setBranchid(newBranchId);
				sysAccountDao.updateByPrimaryKey(sysAccount);
			}
		}
	}
}
