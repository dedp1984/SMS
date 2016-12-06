package com.pujjr.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.pujjr.custom.Result;

@Controller
public class BatchFileUploadController
{
	@RequestMapping("/upload")
	@ResponseBody
	public Result uploadFile(MultipartFile file)
	{
		return new Result(true);
	}
}
