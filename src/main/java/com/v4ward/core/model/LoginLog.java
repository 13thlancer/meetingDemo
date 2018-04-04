package com.v4ward.core.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

public class LoginLog implements Serializable{

    private static final long serialVersionUID = 1295868625932375219L;

    private String id;

    private String logname;

    private String userid;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createtime;

    private String succeed;

    private String message;

    private String ip;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "LoginLog{" +
                "id='" + id + '\'' +
                ", logname='" + logname + '\'' +
                ", userid='" + userid + '\'' +
                ", createtime=" + createtime +
                ", succeed='" + succeed + '\'' +
                ", message='" + message + '\'' +
                ", ip='" + ip + '\'' +
                '}';
    }
}
