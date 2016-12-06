package com.pujjr.web.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.pujjr.custom.Result;
import com.pujjr.custom.Utils;
import com.pujjr.service.ConvertService;

@Controller
public class ExcelConvertController
{
	@Resource
	private ConvertService convertService;
	@RequestMapping("/excelconvert")
	@ResponseBody
	public Result convertDKExcelToTxt(MultipartFile file,
									  String startSeq,
									  HttpSession session)
	{
		String convertFilePath;
		try
		{
			String newFileName=file.getOriginalFilename();
			String absFilePath=session.getServletContext().getRealPath("/")+"upload"+File.separator+newFileName;
			FileUtils.copyInputStreamToFile(file.getInputStream(), new File(absFilePath));
			convertFilePath=convertService.convertDKExcelToText(absFilePath,startSeq);
		}catch(Exception e)
		{
			Result result=new Result(false);
			result.addErros("errmsg", e.getMessage());
			return result;
		}
		Result result=new Result(true);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("convertFilePath", convertFilePath);
		result.setItems(map);
		return result;
	}
}
