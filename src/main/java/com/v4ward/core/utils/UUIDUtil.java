package com.v4ward.core.utils;

import java.rmi.dgc.VMID;
import java.util.UUID;

public class UUIDUtil {

	/**
	 * 获取随机唯一ID
	 */
	public final static String getUUID() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 生成一个全球唯一的ID值
	 */
	public final static String generateUniqueKey() {
		return new VMID().toString();
	}

}
