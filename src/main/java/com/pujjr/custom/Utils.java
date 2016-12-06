package com.pujjr.custom;

import java.io.File;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.util.NumberToTextConverter;

import com.pujjr.domain.SysAccount;
import com.pujjr.domain.SysAccountFeatureKey;

public class Utils {
	/**
	 * ��ȡ��������
	 * **/
	public static int getSpaceDay(Date beginDate,Date endDate)
	{
		return (int) ((endDate.getTime()-beginDate.getTime())/(24*60*60*1000)+1);
	}
	/**
	 * ����ָ����ʽ��ȡ��ǰ����
	 * **/
	public static String getCurrentTime(String format)
	{
		if(format==null||format==""||format.length()==0)
		{
			format="yyyyMMddHHmmss";
		}
		SimpleDateFormat df = new SimpleDateFormat(format);//�������ڸ�ʽ
		return df.format(new Date());
	}
	/**
	 * �������ڻ�ȡ���
	 * **/
	public static String getYear(Date date)
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy");//�������ڸ�ʽ
		return df.format(date);
	}
	/**
	 * �������ڻ�ȡ�·�
	 * **/
	public static String getMonth(Date date)
	{
		SimpleDateFormat df = new SimpleDateFormat("MM");//�������ڸ�ʽ
		return df.format(date);
	}
	/**
	 * ����ת�ַ���
	 * **/
	public static String getFormatDate(Date date,String format)
	{
		SimpleDateFormat df = new SimpleDateFormat(format);//�������ڸ�ʽ
		return df.format(date);
	}
	/**
	 * ������ݻ�ȡ��������
	 * **/
	public static int getYearDays(String year)
	{
		if((Integer.valueOf(year)/4==0&&Integer.valueOf(year)/100!=0)||(Integer.valueOf(year)/400==0))
		{
			return 366;
		}
		else
		{
			return 365;
		}
	}
	/**
	 * 8�����ַ���ת���ڸ�ʽ
	 * @throws ParseException 
	 * **/
	public static Date str82date(String date) throws ParseException
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//�������ڸ�ʽ
		return df.parse(date);
	}
	public static Timestamp str2time(String time) throws ParseException
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");//�������ڸ�ʽ
		return new Timestamp((df.parse(time)).getTime());
	}
	public static String get16UUID()
	{
		String uuid=UUID.randomUUID().toString();
		byte[] outputByteArray;
		 try {
			MessageDigest messageDigest =MessageDigest.getInstance("MD5");
			byte[] inputByteArray=uuid.getBytes();
			messageDigest.update(inputByteArray);
			outputByteArray=messageDigest.digest();
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} 
		 StringBuffer buf = new StringBuffer("");
         for (int offset = 0; offset < outputByteArray.length; offset++) {
            int  i = outputByteArray[offset];
             if (i < 0)
                 i += 256;
             if (i < 16)
                 buf.append("0");
             buf.append(Integer.toHexString(i));
         }
        return buf.toString().substring(8,24);
	}
	
	public static String getFileSuffix(String fileName)
	{
		int indexSuffix;
		if((indexSuffix=fileName.lastIndexOf("."))!=-1)
		{
			return fileName.substring(indexSuffix, fileName.length());
		}
		return fileName;
	}
	public static String getFileName(String filePath)
	{
		int indexSlash;
		if((indexSlash=filePath.lastIndexOf(File.separator))!=-1)
		{
			return filePath.substring(indexSlash+1, filePath.length());
		}
		return filePath;
	}
	
	public static String getFilePath(String filePath)
	{
		int indexSlash;
		if((indexSlash=filePath.lastIndexOf(File.separator))!=-1)
		{
			return filePath.substring(0, indexSlash);
		}
		return null;
	}
	public static ArrayList<String> getAccoutTypeListByFeatures(List<SysAccountFeatureKey> busifeatures)
	{
		ArrayList<String> typelist=new ArrayList<String>();
		for(int i=0;i<busifeatures.size();i++)
		{
			SysAccountFeatureKey feature=busifeatures.get(i);
			if(feature.getType().equals("1"))
			{
				if(feature.getValue().equals("1"))
				{
					typelist.add(AccountType.PUBLIC_CURRENT_ACCOUNT);
					typelist.add(AccountType.PUBLIC_FIXED_ACCOUNT);
					typelist.add(AccountType.PUBLIC_CREDIT_ACCOUNT);
					typelist.add(AccountType.PUBLIC_FINANCE_ACCOUNT);
				}
				else
				{
					typelist.add(AccountType.PERSON_CURRENT_ACCOUNT);
					typelist.add(AccountType.PERSON_FINANCE_ACCOUNT);
					typelist.add(AccountType.PERSON_FIXED_ACCOUNT);
					typelist.add(AccountType.PERSON_PLEDGE_ACCOUNT);
				}
			}
		}
		return typelist;
	}
	public static ArrayList<String> getFinanceAccoutTypeListByFeatures(List<SysAccountFeatureKey> busifeatures)
	{
		ArrayList<String> typelist=new ArrayList<String>();
		typelist.add(AccountType.PUBLIC_FINANCE_ACCOUNT);
		typelist.add(AccountType.PERSON_FINANCE_ACCOUNT);
		/*
		for(int i=0;i<busifeatures.size();i++)
		{
			SysAccountFeatureKey feature=busifeatures.get(i);
			if(feature.getType().equals("1"))
			{
				if(feature.getValue().equals("1"))
				{
					typelist.add(AccountType.PUBLIC_FINANCE_ACCOUNT);
				}
				else
				{
					typelist.add(AccountType.PERSON_FINANCE_ACCOUNT);
				}
			}
		}*/
		return typelist;
	}
	

	
	public static boolean isAuthorQueryAllBranchData(SysAccount sysAccount)
	{
		switch(Integer.valueOf(sysAccount.getProperty()))
		{
			case  1:
			case  6:
				return true;
			case  3:
			case  4:
			case  5:
			case  2:
				return false;
			default:
				return false;
		}
	}
	
	public static boolean isAuthorQueryOneSelfData(SysAccount sysAccount)
	{
		switch(Integer.valueOf(sysAccount.getProperty()))
		{
			case  1:
			case  2:
			case  6:
				return false;
			case  3:
			case  4:
			case  5:
				return true;
			default:
				return true;
		}
	}
	
	public static String getCellValue(Cell cell)
	{
		switch (cell.getCellType())
		{
		case Cell.CELL_TYPE_STRING:
			return cell.getRichStringCellValue().getString();
		case Cell.CELL_TYPE_NUMERIC:
			short format=cell.getCellStyle().getDataFormat(); 
			if(format == 14 || format == 31 || format == 57 || format == 58)
			{
				double value = cell.getNumericCellValue();  
			    Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value); 
			    return Utils.getFormatDate(date,"yyyy��MM��dd��");
			}
//			if (DateUtil.isCellDateFormatted(cell))
//			{
//				Date date=cell.getDateCellValue();
//				return Utils.getFormatDate(date,"yyyyMMdd");
//			}
			else
			{
				return NumberToTextConverter.toText(cell.getNumericCellValue());
				//BigDecimal bd = new BigDecimal(cell.getNumericCellValue());
				//return bd.toPlainString();
			}
		case Cell.CELL_TYPE_BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());
		case Cell.CELL_TYPE_FORMULA:
			return cell.getCellFormula();
		default:
			return "";
		}
	}
}
