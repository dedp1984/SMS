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
		// ����������
		wb = WorkbookFactory.create(new File(filePath));
		// ��ȡexcelҳ
		Sheet sheet = wb.getSheetAt(0);
		// ��ȡ������
		int rowCnt = sheet.getLastRowNum();
		
		int iStartSeq=Integer.valueOf(startSeq);
		
		ArrayList<String> list=new ArrayList<String>();
		
		for(int i=2;i<=rowCnt;i++)
		{
			Row row=sheet.getRow(i);
			//����
			String custname=Utils.getCellValue(row.getCell(3));
			//���֤
			String idno=Utils.getCellValue(row.getCell(5));
			//��������
			String bankname=Utils.getCellValue(row.getCell(6));
			
			Bankno bankno=banknoDao.selelctByBankname(bankname);
			if(bankno==null)
			{
				throw new Exception("��"+(i+1)+"�С�"+bankname+"��δ���ö�Ӧ���б�ţ�����ϵ����Ա");
			}
			//���п���
			String cardno=Utils.getCellValue(row.getCell(7));
			//���۽��
			String amt=Utils.getCellValue(row.getCell(10));
			
			int iAmt=(int)(Double.valueOf(amt)*100);
			
			
			//֧����ˮ��
			String tranSeq=String.format("%016d", iStartSeq);
			//��ǰ����
			String tranDate=Utils.getCurrentTime("yyyMMdd");
			
			String tranTxt=tranDate+"|"+tranSeq+"|"+bankno.getBankno()+"|0|"+cardno+"|"+custname+"|01|"+idno+"|"+iAmt+"|"+"���۽���|1��";
			

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
