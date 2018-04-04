package com.v4ward.core.model;

import java.io.Serializable;

public class ManagerOrg implements Serializable {

    private static final long serialVersionUID = 7487526551173643538L;

    public String id;

    public String orgId;

    public String managerId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }
}
