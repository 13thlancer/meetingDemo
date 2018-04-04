package com.v4ward.core.tips;

public class UnAuthTip extends Tip {

    public UnAuthTip() {
        super.code = 403;
        super.message = "无权限,请联系管理员！";
    }
}
