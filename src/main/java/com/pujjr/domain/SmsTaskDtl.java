package com.pujjr.domain;

public class SmsTaskDtl {
    private String id;

    private String taskid;

    private String content;

    private String tel;

    private String procstatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
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
}