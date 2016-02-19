package com.pujjr.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pujjr.custom.Utils;
import com.pujjr.dao.BanknoMapper;
import com.pujjr.domain.Bankno;

@Service
public class ConvertService
{
	@Resource
	private BanknoMapper banknoDao;
	
	public String convertDKExcelToText(String filePath,String startSeq) throws Exception
	{
		Workbook wb = null;
		// 创建工作簿
		wb = WorkbookFactory.create(new File(filePath));
		// 获取excel页
		Sheet sheet = wb.getSheetAt(0);
		// 获取总行数
		int rowCnt = sheet.getLastRowNum();
		
		int iStartSeq=Integer.valueOf(startSeq);
		
		ArrayList<String> list=new ArrayList<String>();
		
		for(int i=2;i<=rowCnt;i++)
		{
			Row row=sheet.getRow(i);
			//姓名
			String custname=Utils.getCellValue(row.getCell(3));
			//身份证
			String idno=Utils.getCellValue(row.getCell(5));
			//银行名称
			String bankname=Utils.getCellValue(row.getCell(6));
			
			Bankno bankno=banknoDao.selelctByBankname(bankname);
			if(bankno==null)
			{
				throw new Exception("第"+(i+1)+"行【"+bankname+"】未配置对应银行编号，请联系管理员");
			}
			//银行卡号
			String cardno=Utils.getCellValue(row.getCell(7));
			//代扣金额
			String amt=Utils.getCellValue(row.getCell(10));
			
			int iAmt=(int)(Double.valueOf(amt)*100);
			
			
			//支付流水号
			String tranSeq=String.format("%016d", iStartSeq);
			//当前日期
			String tranDate=Utils.getCurrentTime("yyyMMdd");
			
			String tranTxt=tranDate+"|"+tranSeq+"|"+bankno.getBankno()+"|0|"+cardno+"|"+custname+"|01|"+idno+"|"+iAmt+"|"+"代扣交易|1笔";
			

			iStartSeq++;
			list.add(tranTxt);
			
		}
		String path=Utils.getFilePath(filePath);
		String newFileName=Utils.getFileName(filePath)+"_convert.txt";
		String newFilePath=path+"\\"+newFileName;
		FileUtils.writeLines(new File(newFilePath), list);
		
		return newFileName;
	}
}
