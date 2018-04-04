package com.v4ward.core.poiEntity;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.io.Serializable;
import java.util.List;

public class MenuPoiEntity implements Serializable {

    private static final long serialVersionUID = -6466629959268081444L;


    @Excel(name = "菜单编号", orderNum = "0")
    private String code;

    @Excel(name = "父级菜单编码", orderNum = "1")
    private String pcode;

    @Excel(name = "菜单名称", orderNum = "2")
    private String name;

    @Excel(name = "菜单链接", orderNum = "3")
    private String url;

    @Excel(name = "是否为菜单",replace = {"是_1", "不是_0"}, orderNum = "4")
    private Integer isMenu;

    private List<MenuPoiEntity> children;

    public String getCode() {
        return code;
    }

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<MenuPoiEntity> getChildren() {
        return children;
    }

    public void setChildren(List<MenuPoiEntity> children) {
        this.children = children;
    }

    public Integer getIsMenu() {
        return isMenu;
    }

    public void setIsMenu(Integer isMenu) {
        this.isMenu = isMenu;
    }

    public static MenuPoiEntity newInstance(String code,String pcode,String name,String url,Integer isMenu){
        MenuPoiEntity mpe = new MenuPoiEntity();
        mpe.setCode(code);
        mpe.setPcode(pcode);
        mpe.setName(name);
        mpe.setUrl(url);
        mpe.setIsMenu(isMenu);
        return mpe;
    }
}
