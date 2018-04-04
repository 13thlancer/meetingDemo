package com.v4ward.meeting.model;

import java.util.Date;

public class SysResources {
    private String id;

    private String partmeetingId;

    private String resourcesTitle;

    private String resourcesType;

    private Double resourcesSize;

    private Integer resourcesStatus;

    private String createEmp;

    private Date createTime;

    private String updateEmp;

    private Date updateTime;

    private Integer version;

    private String resourcesPath;
    
    private Integer orderCode;

    
    /**
	 * @return the orderCode
	 */
	public Integer getOrderCode() {
		return orderCode;
	}

	/**
	 * @param orderCode the orderCode to set
	 */
	public void setOrderCode(Integer orderCode) {
		this.orderCode = orderCode;
	}

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

    public String getResourcesTitle() {
        return resourcesTitle;
    }

    public void setResourcesTitle(String resourcesTitle) {
        this.resourcesTitle = resourcesTitle == null ? null : resourcesTitle.trim();
    }

    public String getResourcesType() {
        return resourcesType;
    }

    public void setResourcesType(String resourcesType) {
        this.resourcesType = resourcesType == null ? null : resourcesType.trim();
    }

    public Double getResourcesSize() {
        return resourcesSize;
    }

    public void setResourcesSize(Double resourcesSize) {
        this.resourcesSize = resourcesSize;
    }

    public Integer getResourcesStatus() {
        return resourcesStatus;
    }

    public void setResourcesStatus(Integer resourcesStatus) {
        this.resourcesStatus = resourcesStatus;
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

    public String getResourcesPath() {
        return resourcesPath;
    }

    public void setResourcesPath(String resourcesPath) {
        this.resourcesPath = resourcesPath == null ? null : resourcesPath.trim();
    }
}