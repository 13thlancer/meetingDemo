package com.v4ward.core.model;

import java.io.Serializable;

public class Dict implements Serializable {

    private static final long serialVersionUID = 7181865955280808693L;

    private String id;

    private String num;

    private String pid;

    private String name;

    private String tips;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    @Override
    public String toString() {
        return "Dict{" +
                "id='" + id + '\'' +
                ", num='" + num + '\'' +
                ", pid='" + pid + '\'' +
                ", name='" + name + '\'' +
                ", tips='" + tips + '\'' +
                '}';
    }
}
