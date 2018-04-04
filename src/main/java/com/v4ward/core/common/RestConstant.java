package com.v4ward.core.common;

public class RestConstant {

	public static final Integer SUCCESS_CODE = 200;

	public static final Integer Failure_CODE = 500;

	public static final String LOGIN_SUCCESS_DESC = "登录成功";

	public static final String UPDATEPWD_SUCCESS_DESC = "登录成功";

	public static final String UPLOAD_IMG_SUCCESS_DESC = "头像上传成功";

	public static final Integer TOKEN_NOT_MATCH_CODE = 4001;

	public static final String TOKEN_NOT_MATCH_DESC = "无效或非法token,请重新获取";

	public static final Integer TOKEN_TIME_EXPIRED_CODE = 4002;

	public static final String TOKEN_TIME_EXPIRED_DESC = "token已过期,请重新获取";

	public static final Integer TOKEN_NOT_EXISTS_CODE = 4003;

	public static final String TOKEN_NOT_EXISTS_DESC = "请求头未包含token";

	public static final Integer LOGIN_FAIL_NOT_FOUND_MOBILE_CODE = 5001;

	public static final String LOGIN_FAIL_NOT_FOUND_MOBILE_DESC = "该手机号还没有注册";

	public static final Integer LOGIN_FAIL_WRONG_PASSWORD_CODE = 5002;

	public static final String LOGIN_FAIL_WRONG_PASSWORD_DESC = "密码错误";

	public static final Integer UPLOAD_IMG_ERROR_CODE = 5003;

	public static final String UPLOAD_IMG_ERROR_DESC = "头像上传失败";

	public static final Integer UPLOAD_IMG_IS_EMPTY_CODE = 5004;

	public static final String UPLOAD_IMG_IS_EMPTY_DESC = "图片不能为空";

	public static final Integer SYSTEM_ERROR_CODE = 5010;

	public static final String SYSTEM_ERROR_DESC = "系统繁忙，请稍后再试";

	public static final String GET_PIC_SUCCESS = "获取头像成功";

}
