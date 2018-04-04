package com.v4ward.meeting.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class SysSubmeeting implements Serializable{
	
	
    /**
	 * 
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 */
	private static final long serialVersionUID = -7915442641384138503L;

	private String id;

    private String partmeetingId;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date starttime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endtime;

    private String submeetingTheme;

    private String submeetingHost;

    private String submeetingLocation;

    private Integer submeetingStatus;

    private Integer orderCode;

    private String createEmp;

    private Date createTime;

    private String updateEmp;

    private Date updateTime;

    private Integer version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getPartmeetingId() {
        return partmeetingId;
    }

    public void setPartmeetingId(String partmeetingId) {
        this.partmeetingId = partmeetingId == null ? null : partmeetingId.trim();
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public String getSubmeetingTheme() {
        return submeetingTheme;
    }

    public void setSubmeetingTheme(String submeetingTheme) {
        this.submeetingTheme = submeetingTheme == null ? null : submeetingTheme.trim();
    }

    public String getSubmeetingHost() {
        return submeetingHost;
    }

    public void setSubmeetingHost(String submeetingHost) {
        this.submeetingHost = submeetingHost == null ? null : submeetingHost.trim();
    }

    public String getSubmeetingLocation() {
        return submeetingLocation;
    }

    public void setSubmeetingLocation(String submeetingLocation) {
        this.submeetingLocation = submeetingLocation == null ? null : submeetingLocation.trim();
    }

    public Integer getSubmeetingStatus() {
        return submeetingStatus;
    }

    public void setSubmeetingStatus(Integer submeetingStatus) {
        this.submeetingStatus = submeetingStatus;
    }

    public Integer getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(Integer orderCode) {
        this.orderCode = orderCode;
    }

    public String getCreateEmp() {
        return createEmp;
    }

    public void setCreateEmp(String createEmp) {
        this.createEmp = createEmp == null ? null : createEmp.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateEmp() {
        return updateEmp;
    }

    public void setUpdateEmp(String updateEmp) {
        this.updateEmp = updateEmp == null ? null : updateEmp.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}