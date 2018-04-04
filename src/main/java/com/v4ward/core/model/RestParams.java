package com.v4ward.core.model;

import java.io.Serializable;

public class RestParams implements Serializable{

    private static final long serialVersionUID = -6865182175268532893L;

    private Integer code;

    private String desc;

    private Object data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static RestParams newInstance(Integer code, String desc, Object data){
        RestParams restParams = new RestParams();
        restParams.setCode(code);
        restParams.setDesc(desc);
        restParams.setData(data);
        return restParams;
    }
}
