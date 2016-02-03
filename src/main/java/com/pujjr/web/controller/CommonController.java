package com.pujjr.web.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pujjr.custom.Utils;


@Controller
public class CommonController
{
	@RequestMapping("/download")
	@ResponseBody
	public ResponseEntity<byte[]> ExportBindAccount(String path,HttpSession session) throws IOException
	{
		path=new String(path.getBytes("ISO8859-1"),"UTF-8");
		String filePath=session.getServletContext().getRealPath("/")+path;
		HttpHeaders headers = new HttpHeaders();  
	    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);  
	    String outputName=Utils.getFileName(path);
	    headers.setContentDispositionFormData("attachment", new String(outputName.getBytes("GB2312"), "ISO_8859_1"));  
	    return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(new File(filePath)),  
	                                      headers, HttpStatus.OK); 
	}
}
