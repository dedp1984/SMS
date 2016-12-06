package com.pujjr.domain;

import java.sql.Timestamp;
import java.util.Date;

public class SmsSended {

	private String id;

    private String srcchnl;

    private String detailid;

    private String content;

    private String tel;

    private String procstatus;

    private String sendtaskid;

    private Timestamp sendtime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSrcchnl() {
        return srcchnl;
    }

    public void setSrcchnl(String srcchnl) {
        this.srcchnl = srcchnl;
    }

    public String getDetailid() {
        return detailid;
    }

    public void setDetailid(String detailid) {
        this.detailid = detailid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getProcstatus() {
        return procstatus;
    }

    public void setProcstatus(String procstatus) {
        this.procstatus = procstatus;
    }

    public String getSendtaskid() {
        return sendtaskid;
    }

    public void setSendtaskid(String sendtaskid) {
        this.sendtaskid = sendtaskid;
    }

    public Timestamp getSendtime() {
        return sendtime;
    }

    public void setSendtime(Timestamp sendtime) {
        this.sendtime = sendtime;
    }
}