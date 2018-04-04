package com.v4ward.core.model;

import java.io.Serializable;

public class RoleMenu implements Serializable{

    private static final long serialVersionUID = -8980282851836932957L;

    private String id;

    private String menuid;

    private Integer rolenum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMenuid() {
        return menuid;
    }

    public void setMenuid(String menuid) {
        this.menuid = menuid;
    }

    public Integer getRolenum() {
        return rolenum;
    }

    public void setRolenum(Integer rolenum) {
        this.rolenum = rolenum;
    }
}