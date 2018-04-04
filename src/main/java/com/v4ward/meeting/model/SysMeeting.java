package com.v4ward.meeting.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class SysMeeting {
    private String id;

    private String meetingTheme;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date starttime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endtime;

    private String meetingLocation;

    private Integer meetingStatus;

    private String superOrgId;
    
    private String createEmp;

    private Date createTime;

    private String updateEmp;

    private Date updateTime;

    private Integer version;
    
    private String meetingBrief;

    private String meetingNotice;

    private Integer deleteStatus;

    /**
	 * @return the deleteStatus
	 */
	public Integer getDeleteStatus() {
		return deleteStatus;
	}

	/**
	 * @param deleteStatus the deleteStatus to set
	 */
	public void setDeleteStatus(Integer deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getMeetingTheme() {
        return meetingTheme;
    }

    public void setMeetingTheme(String meetingTheme) {
        this.meetingTheme = meetingTheme == null ? null : meetingTheme.trim();
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

    public String getMeetingLocation() {
        return meetingLocation;
    }

    public void setMeetingLocation(String meetingLocation) {
        this.meetingLocation = meetingLocation == null ? null : meetingLocation.trim();
    }

    public Integer getMeetingStatus() {
        return meetingStatus;
    }

    public void setMeetingStatus(Integer meetingStatus) {
        this.meetingStatus = meetingStatus;
    }

    public String getSuperOrgId() {
        return superOrgId;
    }

    public void setSuperOrgId(String superOrgId) {
        this.superOrgId = superOrgId == null ? null : superOrgId.trim();
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
    
    public String getMeetingBrief() {
        return meetingBrief;
    }

    public void setMeetingBrief(String meetingBrief) {
        this.meetingBrief = meetingBrief == null ? null : meetingBrief.trim();
    }

    public String getMeetingNotice() {
        return meetingNotice;
    }

    public void setMeetingNotice(String meetingNotice) {
        this.meetingNotice = meetingNotice == null ? null : meetingNotice.trim();
    }
}