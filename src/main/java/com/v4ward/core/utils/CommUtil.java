package com.v4ward.core.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 对比两个对象的变化的工具类
 *
 * @author fengshuonan
 * @Date 2017/3/31 10:36
 */
public class CommUtil {

	public final static String SUCCESS = "200";
	public final static String FAIL = "500";

	/**
	 * 组装返回包
	 * 
	 * @param code
	 *            返回码
	 * @param desc
	 *            描述
	 * @param data
	 *            数据
	 */
	public final static Map<String, Object> setMessage(String code, String desc, Object data) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", code);
		map.put("desc", desc);
		map.put("data", data);
		return map;
	}
}