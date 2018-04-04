package com.v4ward.core.common;

public class AuthConstant {
	
	// 第三方用户唯一凭证   
    public static final String appKey  = "b395e66de5d9458b8b8eeedd602ce7d3";
	//public static final String appKey  = "82401784ff1345fd8b7baff0910b664b";
    
    // 第三方用户唯一凭证密钥
    public static final String appSecret  = "592cf42e02304aeea3d2ca4208fc7923";
	//public static final String AppSecret  = "6c409420d1274904bf5f70ce05d00c1e";
    
    // http语义--GET
    public static final String GET_METHOD  = "GET";
    
   // http语义--POST
    public static final String POST_METHOD  = "POST";
    
    //获取time的url
  	public static final String get_validate_time = "https://pdata.4ggogo.com/web-in/validate/time.html";
    //public static final String get_validate_time = "https://tj.4ggogo.com/web-in/validate/time.html";
  	
    //获取token的url
  	 public static final String get_token = "https://pdata.4ggogo.com/web-in/core.html";
  	//public static final String get_token = "https://tj.4ggogo.com/web-in/auth.html";
  	
    //获取products的url
  	public static final String get_products = "https://pdata.4ggogo.com/web-in/products.html";
  	//public static final String get_products = "https://tj.4ggogo.com/web-in/products.html";
  	
    //获取charge的url
  	public static final String get_charge = "https://pdata.4ggogo.com/web-in/boss/charge.html";
  	//public static final String get_charge = "https://tj.4ggogo.com/web-in/boss/charge.html";
  	
    //本地模拟下游回调接口
  	public static final String localCallBack = "http://localhost:8060/localCallBack.html";
  	
  	//redis缓存过期时间
  	public static final Integer REDIS_OVER_TIME_ = 25;
  	
  	//上游、下游token
  	public static final String cmcc_token_key = "cmccKey";
  	
  	public static final String ec_token_key = "ecKey";
  	
  	public static final int HTTP_SC_FORBIDDEN = 403;
  	
  	public static final int HTTP_INTERAL_ERROR = 500;
  	
  	public static final int HTTP_OK = 200;
  	
    //-----------------------------------下游提示语-------------------------------------------------  	
    
	// 成功返回码
	public final static String SUCCESS_CODE = "00000";
	public static final String SUCCESS_CHARGE_MSG = "订单提交成功,正在处理中！";

//	public static final String SUCCESS_CHARGE_ORDER_MSG = "订单提交成功,正在处理中！";
 	
	public static final String SUCCESS_CHARGE_RECORD_MSG = "充值成功！";
	
	
	public static final String SUCCESS_BALANCED_MSG = "余额查询成功！";
	
	
 	//时间格式错误
 	public final static String TIME_NOT_MATCH_CODE = "10010";
  	public static final String TIME_NOT_MATCH_MSG = "时间格式错误，请按规定时间格式传输";

    //换取token时的签名错误
   	public final static String SIGN_NOT_MATCH_CODE = "10020";
  	public static final String SIGN_NOT_MATCH_MSG = "获取token所用的签名错误，请检查签名算法正确性";
  	
    //无效或非法的token
  	public static final String  TOKEN_NOT_MATCH_CODE = "10030";
  	public static final String  TOKEN_NOT_MATCH_MSG = "无效或非法token,请重新获取";
  	
  	//token已过期
  	public static final String  TOKEN_TIME_EXPIRED_CODE = "10040";
  	public static final String  TOKEN_TIME_EXPIRED_MSG = "token已过期,请重新获取";
  	
    //请求头中未携带token	
  	public static final String  TOKEN_NOT_EXIST_CODE = "10050";
  	public static final String  TOKEN_NOT_EXIST_MSG = "请求头中未携带token";
  	
  	public final static String SIGNATURE_NOT_MATCH_CODE = "10060";
  	public static final String SIGNATURE_NOT_MATCH_MSG = "请求头中携带的签名错误，请检查签名算法正确性";
  	
  	public static final String NOT_ENOUGH_CODE = "10070";
  	public static final String NOT_ENOUGH_MSG = "商户流量池余额不足，请尽快充值！";
  	
  	public static final String CP_COMMIT_FAIL_CODE = "10080";
  	public static final String CP_COMMIT_FAIL_MSG = "平台维护中，请稍后再试";

	public static final String SERIALNUM_ERROR_CODE = "10081";
	public static final String SERIALNUM_ERROR_MSG = "下游流水号重复，请核对后重试";

	public static final String PRODUCT_NOT_MATCH_CODE = "10082";
	public static final String PRODUCT_NOT_MATCH_MSG = "无此类产品信息";
  	
  	public static final String FAILED_CHARGE_RECORD_CODE = "10090";
  	public static final String FAILED_CHARGE_RECORD_MSG = "充值失败！";

	public static final String FAILED_SERIALNUM_NOT_FOUND_CODE = "10100";
	public static final String FAILED_SERIALNUM_NOT_FOUND_MSG = "下游流水号不存在！";

  	public static final String SERVER_ERROR_CODE = "30000";
  	public static final String SERVER_ERROR_MSG = "解析报文出现异常，请检查报文格式";
  	
  	
  	
  	
	//public static final String FAILED_CHARGE_MSG = "订单提交失败！";
	//public static final String PROXY_FULL_MSG = "流量池余额不足！";
	//public static final String FAIL_PRODUCT_MSG= "查询产品失败！";
	//public static final String FAILED_BALANCED_MSG = "余额查询失败！";
  	

}
