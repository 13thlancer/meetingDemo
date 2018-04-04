/**   
 * Copyright © 2018 武汉现代物华科技发展有限公司信息分公司. All rights reserved.
 * 
 * @Title: UploadUtil.java 
 * @Prject: wisdri-meeting
 * @Package: com.v4ward.core.utils
 * @author: 彭浩
 * @date: 2018年3月19日 上午8:48:34 
 * @version: V1.0   
 */
package com.v4ward.core.utils;

import java.io.File;
import java.io.IOException;

import org.springframework.boot.ApplicationHome;
import org.springframework.web.multipart.MultipartFile;

import com.v4ward.core.common.FileConstant;

/** 
 * 上传公共方法
 * @ClassName: UploadUtil
 * @author: 彭浩
 * @date: 2018年3月19日 上午8:48:34  
 */
public class UploadUtil {

	/** 
	 * 上传方法，jar包直接访问
	 * @Title: upload
	 * @param file  MultipartFile
	 * @param suffixPath  文件路径路径后缀  如pdf,img 不包含左右分隔符
	 * @param storeFileName	实际存储的文件名
	 * @return	存储文件的相对路径  /file/...
	 * @return: String
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	public static String upload(MultipartFile file, String suffixPath, String storeFileName)
			throws IllegalStateException, IOException {
		
		if (file.isEmpty()) {
			return "";
		}

		ApplicationHome home = new ApplicationHome(UploadUtil.class);
		File jarFile = home.getSource();
		String jarPath = jarFile.getParentFile().getAbsolutePath();
		String stroeFilePath = FileConstant.SEPARATOR + suffixPath + FileConstant.SEPARATOR + storeFileName;
		File dest = new File(jarPath + stroeFilePath);

		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdirs();
		}
		file.transferTo(dest);
		
		return stroeFilePath;
	}

}
