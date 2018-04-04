package com.v4ward.core.model;

import java.io.Serializable;
import java.util.Date;

//记录用户换号前的手机号
public class ManagerUsedPhone implements Serializable {

    private static final long serialVersionUID = -5902731616256787343L;

    private String id;

    private String managerId;

    private String usedPhone;

    private Date startTime;

    private Date endTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getUsedPhone() {
        return usedPhone;
    }

    public void setUsedPhone(String usedPhone) {
        this.usedPhone = usedPhone;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
