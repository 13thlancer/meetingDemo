package com.v4ward.core.utils;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class ImUtil {

	private static Log log = LogFactory.getLog(ImUtil.class.getSimpleName());

	/**
	 * 对云信服务端发起post请求
	 * 
	 * @param url
	 * @param paramMap
	 * @param appKey
	 * @param appSecret
	 */
	public static String post(String url, Map<String, String> paramMap, String appKey, String appSecret)
			throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String nonce = getNonceStr();
		String curTime = String.valueOf((new Date()).getTime() / 1000L);
		String checkSum = getCheckSum(appSecret, nonce, curTime);

		// 设置请求的header
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("AppKey", appKey);
		httpPost.addHeader("Nonce", nonce);
		httpPost.addHeader("CurTime", curTime);
		httpPost.addHeader("CheckSum", checkSum);
		httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

		// 设置请求的参数
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for (Entry<String, String> ent : paramMap.entrySet()) {
			nvps.add(new BasicNameValuePair(ent.getKey(), ent.getValue()));
		}
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

		// 执行请求
		CloseableHttpResponse response = httpclient.execute(httpPost);

		String res = EntityUtils.toString(response.getEntity(), "utf-8");
		log.info("IM请求结果：" + res);
		return res;
	}

	private static String getNonceStr() {
		return UUIDUtil.generateUniqueKey();
	}

	// 计算并获取CheckSum
	private static String getCheckSum(String appSecret, String nonce, String curTime) {
		return encode("sha1", appSecret + nonce + curTime);
	}

	// 计算并获取md5值
	public static String getMD5(String requestBody) {
		return encode("md5", requestBody);
	}

	private static String encode(String algorithm, String value) {
		if (value == null) {
			return null;
		}
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			messageDigest.update(value.getBytes());
			return getFormattedText(messageDigest.digest());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static String getFormattedText(byte[] bytes) {
		int len = bytes.length;
		StringBuilder buf = new StringBuilder(len * 2);
		for (int j = 0; j < len; j++) {
			buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
			buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
		}
		return buf.toString();
	}

	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
			'e', 'f' };
}
