package com.v4ward.core.poiEntity;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.io.Serializable;
import java.util.List;

public class OrgPoiEntity implements Serializable {

    private static final long serialVersionUID = 8106149065645751633L;

    @Excel(name = "组织编码", orderNum = "0")
    private String code;

    @Excel(name = "父级组织编码", orderNum = "1")
    private String pcode;

    @Excel(name = "组织名称", orderNum = "2")
    private String name;

    private List<OrgPoiEntity> children;

    public String getCode() {
        return code; }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPcode() {
        return pcode;
    }

    public void setPcode(String pcode) {
        this.pcode = pcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<OrgPoiEntity> getChildren() {
        return children;
    }

    public void setChildren(List<OrgPoiEntity> children) {
        this.children = children;
    }

    public static OrgPoiEntity newInstance(String code,String pcode,String name){
        OrgPoiEntity ope = new OrgPoiEntity();
        ope.setCode(code);
        ope.setPcode(pcode);
        ope.setName(name);
        return ope;
    }
}
