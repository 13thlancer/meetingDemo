package com.v4ward.core.tips;

/**
 * 返回给前台的错误提示
 *
 * @author fengshuonan
 * @date 2016年11月12日 下午5:05:22
 */
public class ErrorTip extends Tip {

    public ErrorTip(int code, String message) {
        super();
        this.code = code;
        this.message = message;
    }
    
    public ErrorTip(int code, String message ,String desc) {
        super();
        this.code = code;
        this.message = message;
        this.desc = desc;
    }
}
