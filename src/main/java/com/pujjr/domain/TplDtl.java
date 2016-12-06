package com.pujjr.domain;

public class TplDtl extends TplDtlKey {
    private String comment;

	private String colnum;

	private String varname;

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getColnum() {
		return colnum;
	}

	public void setColnum(String colnum) {
		this.colnum = colnum;
	}

	public String getVarname() {
		return varname;
	}

	public void setVarname(String varname) {
		this.varname = varname;
	}
}