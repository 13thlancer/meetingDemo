package com.v4ward.core.poiEntity;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.io.Serializable;

public class OrgPoiExportEntity implements Serializable {
    private static final long serialVersionUID = 7001095341732811002L;

    @Excel(name = "组织编码", orderNum = "0")
    private String num;

    @Excel(name = "父级组织编码", orderNum = "1")
    private String pnum;

    @Excel(name = "组织名称", orderNum = "2")
    private String name;

    @Excel(name = "组织全称", orderNum = "3")
    private String fullNames;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getPnum() {
        return pnum;
    }

    public void setPnum(String pnum) {
        this.pnum = pnum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullNames() {
        return fullNames;
    }

    public void setFullNames(String fullNames) {
        this.fullNames = fullNames;
    }
}
