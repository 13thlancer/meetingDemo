package com.v4ward.core.utils;

import java.security.MessageDigest;
import java.util.Random;

public class Md5Utils {
	
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
			"e", "f" };

	private final static int digit = 32;

	
	/** 
	 * 获取48位随机salt
	 * @Title: getSalt
	 * @return
	 * @return: String
	 */
	public static String getSalt() {
		
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < digit; i++) {
			sb.append(hexDigits[new Random().nextInt(hexDigits.length)]);
		}
		return sb.toString();
	}
	
	

	/** 
	 * 根据明文和salt经MD5加密得到密文
	 * @Title: encrypt
	 * @param password
	 * @param salt
	 * @return
	 * @return: String
	 */
	public static String encrypt(String password,String salt) {
		String result = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			// 加密后的字符串
			result = byteArrayToHexString(md.digest(mergePasswordAndSalt(password,salt).getBytes("UTF-8")));
		} catch (Exception ex) {
			result = "";
		}
		return result;
	}

	
	/** 
	 * 验证密码
	 * @Title: isPasswordValid
	 * @param encPass  密文
	 * @param rawPass  明文
	 * @param salt 
	 * @return true:正确   false:错误
	 * @return: boolean
	 */
	public static boolean isPasswordValid(String encPass, String rawPass,String salt) {
		String pass1 = encPass;
		String pass2 = encrypt(rawPass,salt);

		return pass1.equals(pass2);
	}

	private static String mergePasswordAndSalt(String password, String salt) {

		if (password == null) {
			password = "";
		}

		if ((salt == null) || "".equals(salt)) {
			return password;
		} else {
			return password + "{" + salt.toString() + "}";
		}
	}

	/**
	 * 转换字节数组为16进制字串
	 * @param b 字节数组
	 * @return 16进制字串
	 */
	private static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	public static void main(String[] args) {
		
		String salt = getSalt();
		System.out.println("salt=="+salt);
		String rawPass = "passwordadmin";
		String encPass = encrypt(rawPass, salt);
		System.out.println("密文=="+encPass);
		
		System.out.println(isPasswordValid(encPass, rawPass, salt));
		
	}
}