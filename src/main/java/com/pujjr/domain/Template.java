package com.pujjr.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Template {
    private String id;

	private String name;

	private String content;

	private Integer startcolnum;

	private String telvarname;

	private String attachname;

	private String attachpath;

	private Date modifydate;

	private String modifyid;
	
	private List<TplDtl>  detail=new ArrayList<TplDtl>();

	public List<TplDtl> getDetail()
	{
		return detail;
	}

	public void setDetail(List<TplDtl> detail)
	{
		this.detail = detail;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getStartcolnum() {
		return startcolnum;
	}

	public void setStartcolnum(Integer startcolnum) {
		this.startcolnum = startcolnum;
	}

	public String getTelvarname() {
		return telvarname;
	}

	public void setTelvarname(String telvarname) {
		this.telvarname = telvarname;
	}

	public String getAttachname() {
		return attachname;
	}

	public void setAttachname(String attachname) {
		this.attachname = attachname;
	}

	public String getAttachpath() {
		return attachpath;
	}

	public void setAttachpath(String attachpath) {
		this.attachpath = attachpath;
	}

	public Date getModifydate() {
		return modifydate;
	}

	public void setModifydate(Date modifydate) {
		this.modifydate = modifydate;
	}

	public String getModifyid() {
		return modifyid;
	}

	public void setModifyid(String modifyid) {
		this.modifyid = modifyid;
	}
}