package com.v4ward.core.model;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

public class OperationLog implements Serializable{

    private static final long serialVersionUID = -2088878799666296636L;

    private String id;

    private String logtype;

    private String logname;

    private String userid;

    private String classname;

    private String method;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createtime;

    private String succeed;

    private String message;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogtype() {
        return logtype;
    }

    public void setLogtype(String logtype) {
        this.logtype = logtype;
    }

    public String getLogname() {
        return logname;
    }

    public void setLogname(String logname) {
        this.logname = logname;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getSucceed() {
        return succeed;
    }

    public void setSucceed(String succeed) {
        this.succeed = succeed;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "OperationLog{" +
                "id='" + id + '\'' +
                ", logtype='" + logtype + '\'' +
                ", logname='" + logname + '\'' +
                ", userid='" + userid + '\'' +
                ", classname='" + classname + '\'' +
                ", method='" + method + '\'' +
                ", createtime='" + createtime + '\'' +
                ", succeed='" + succeed + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

}
