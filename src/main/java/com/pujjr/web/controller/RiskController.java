package com.pujjr.web.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pujjr.custom.Result;
import com.pujjr.custom.Utils;
import com.pujjr.domain.JXS;
import com.pujjr.domain.SysAccount;
import com.pujjr.domain.Template;
import com.pujjr.service.RiskService;

@Controller
@RequestMapping("/risk")
public class RiskController
{
	
	@Resource
	private RiskService riskSerivce;
	
	@RequestMapping("/queryJXSList")
	@ResponseBody
	public Result queryJXSList(String name ,
									@RequestParam(value="page")String startPage,
									@RequestParam(value="limit")String pageSize,
									HttpSession session)
	{
		SysAccount sysAccount=(SysAccount)session.getAttribute("user");
		PageHelper.startPage(Integer.parseInt(startPage), Integer.parseInt(pageSize),true);
		List<JXS> list=riskSerivce.queryJXSList(name);
		Result result=new Result(true);
		result.setTotalsize( String.valueOf(((Page)list).getTotal()));
		result.setItems(list);
		return result;
	}
	@RequestMapping("/addJXS")
	@ResponseBody
	public Result addJXS(String alias,
			             String fullname,
			             String openbankname,
			             String openbankno,
			             String address,
			             String linkman,
			             String mobile,
			             String telno,
			             String comment,
			             HttpSession session
			             )
	{
		try
		{
			JXS  jxs=new JXS();
			jxs.setId(Utils.get16UUID());
			jxs.setAlias(alias);
			jxs.setFullname(fullname);
			jxs.setOpenbankname(openbankname);
			jxs.setOpenbankno(openbankno);
			jxs.setAddress(address);
			jxs.setLinkman(linkman);
			jxs.setMobile(mobile);
			jxs.setTelno(telno);
			jxs.setComment(comment);
			riskSerivce.addJXS(jxs);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Result result=new Result(false);
			result.addErros("errmsg", e.getMessage());
			return result;
		}
		return new Result(true);
    }
	@RequestMapping("/deleteJXS")
	@ResponseBody
	public Result deleteJXS(String id)
	{
		try
		{
			riskSerivce.deleteJXS(id);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Result result=new Result(false);
			result.addErros("errmsg", e.getMessage());
			return result;
		}
		return new Result(true);
	}
	@RequestMapping("updateJXS")
	@ResponseBody
	public Result updateJXS(String id,
			                String alias, 
			                String fullname, 
			                String openbankname, 
			                String openbankno, 
			                String address, 
			                String linkman, 
			                String mobile, 
			                String telno, 
			                String comment, 
			             HttpSession session)
	{
		try
		{
			JXS jxs = riskSerivce.queryJXSById(id);
			if(jxs==null)
				throw new Exception("经销商不存在");
			jxs.setAlias(alias);
			jxs.setFullname(fullname);
			jxs.setOpenbankname(openbankname);
			jxs.setOpenbankno(openbankno);
			jxs.setAddress(address);
			jxs.setLinkman(linkman);
			jxs.setMobile(mobile);
			jxs.setTelno(telno);
			jxs.setComment(comment);
			riskSerivce.updateJXS(jxs);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Result result = new Result(false);
			result.addErros("errmsg", e.getMessage());
			return result;
		}
		return new Result(true);
	}
}
