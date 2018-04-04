package com.v4ward.core.common;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.v4ward.core.model.UserAccount;
import com.v4ward.core.utils.ImUtil;

import cn.hutool.system.UserInfo;

public class ImComm {
	private static String appKey = "3aa0969198ae5c67e5aa47550dde6a75";// 3aa0969198ae5c67e5aa47550dde6a75
	private static String appSecret = "f160dac1d2f0";// f160dac1d2f0

	@Value("${com.whxx.im.appkey}")
	public static void setAppKey(String appKey) {
		ImComm.appKey = appKey;
	}

	@Value("${com.whxx.im.appsecret}")
	public static void setAppSecret(String appSecret) {
		ImComm.appSecret = appSecret;
	}

	// @Value("${APP_KEY}")
	// private static String appKey;

	// @Value("${APP_SECERT}")
	// private static String appSecret;

	/**
	 * 创建云信账号
	 */
	public final static String createUserAccount(UserAccount po) throws Exception {
		Map<String, String> map = JSONObject.parseObject(JSON.toJSONString(po),
				new TypeReference<Map<String, String>>() {
				});
		String url = ImConst.user_create;
		String res = ImUtil.post(url, map, appKey, appSecret);
		return res;
	}

	/**
	 * 更新云信账号，只能修改props和token
	 */
	public final static String updateUserAccount(UserAccount po) throws Exception {
		Map<String, String> map = JSONObject.parseObject(JSON.toJSONString(po),
				new TypeReference<Map<String, String>>() {
				});
		String url = ImConst.user_update;
		String res = ImUtil.post(url, map, appKey, appSecret);
		return res;
	}

	/**
	 * 更新并获取新token
	 */
	public final static String refreshToken(String accid) throws Exception {
		Map<String, String> map = new HashMap<>();
		map.put("accid", accid);
		String url = ImConst.user_refreshToken;
		String res = ImUtil.post(url, map, appKey, appSecret);
		return res;
	}

	/**
	 * 封禁云信账号
	 */
	public final static String userBlock(String accid) throws Exception {
		Map<String, String> map = new HashMap<>();
		map.put("accid", accid);
		String url = ImConst.user_block;
		String res = ImUtil.post(url, map, appKey, appSecret);
		return res;
	}

	/**
	 * 解禁云信账号
	 */
	public final static String userUnBlock(String accid) throws Exception {
		Map<String, String> map = new HashMap<>();
		map.put("accid", accid);
		String url = ImConst.user_unblock;
		String res = ImUtil.post(url, map, appKey, appSecret);
		return res;
	}

	/**
	 * 更新用户名片
	 */
	public final static String updateUinfo(UserInfo po) throws Exception {
		Map<String, String> map = JSONObject.parseObject(JSON.toJSONString(po),
				new TypeReference<Map<String, String>>() {
				});
		String url = ImConst.user_updateUinfo;
		String res = ImUtil.post(url, map, appKey, appSecret);
		return res;
	}

}
