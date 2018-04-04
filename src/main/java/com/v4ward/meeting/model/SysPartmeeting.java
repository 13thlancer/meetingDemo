package com.v4ward.meeting.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class SysPartmeeting {
    private String id;

    private String partmeetingTitle;

    private String meetingId;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date starttime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endtime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date earlytime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date latesttime;

    private String partmeetingLocation;

    private String partmeetingHost;

    private Integer scanStatus;

    private Integer partmeetingStatus;

    private String createEmp;

    private Date createTime;

    private String updateEmp;

    private Date updateTime;

    private Integer version;

    private Integer lineNum;
    
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
    
    
    /**
	 * @return the lineNum
	 */
	public Integer getLineNum() {
		return lineNum;
	}

	/**
	 * @param lineNum the lineNum to set
	 */
	public void setLineNum(Integer lineNum) {
		this.lineNum = lineNum;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getPartmeetingTitle() {
        return partmeetingTitle;
    }

    public void setPartmeetingTitle(String partmeetingTitle) {
        this.partmeetingTitle = partmeetingTitle == null ? null : partmeetingTitle.trim();
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId == null ? null : meetingId.trim();
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

    public Date getEarlytime() {
        return earlytime;
    }

    public void setEarlytime(Date earlytime) {
        this.earlytime = earlytime;
    }

    public Date getLatesttime() {
        return latesttime;
    }

    public void setLatesttime(Date latesttime) {
        this.latesttime = latesttime;
    }

    public String getPartmeetingLocation() {
        return partmeetingLocation;
    }

    public void setPartmeetingLocation(String partmeetingLocation) {
        this.partmeetingLocation = partmeetingLocation == null ? null : partmeetingLocation.trim();
    }

    public String getPartmeetingHost() {
        return partmeetingHost;
    }

    public void setPartmeetingHost(String partmeetingHost) {
        this.partmeetingHost = partmeetingHost == null ? null : partmeetingHost.trim();
    }

    public Integer getScanStatus() {
        return scanStatus;
    }

    public void setScanStatus(Integer scanStatus) {
        this.scanStatus = scanStatus;
    }

    public Integer getPartmeetingStatus() {
        return partmeetingStatus;
    }

    public void setPartmeetingStatus(Integer partmeetingStatus) {
        this.partmeetingStatus = partmeetingStatus;
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