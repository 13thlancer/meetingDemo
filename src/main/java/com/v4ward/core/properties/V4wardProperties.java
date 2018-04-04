package com.v4ward.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = V4wardProperties.V4WARD_PREFIX)
public class V4wardProperties {
	
	public static final String V4WARD_PREFIX = "v4ward";
	
	private String filePath = "";
	
	private String swaggerEnable = "false";
	
	private String restfulSign = "/rest";

	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * @return the swaggerEnable
	 */
	public String getSwaggerEnable() {
		return swaggerEnable;
	}

	/**
	 * @param swaggerEnable the swaggerEnable to set
	 */
	public void setSwaggerEnable(String swaggerEnable) {
		this.swaggerEnable = swaggerEnable;
	}

	/**
	 * @return the restfulSign
	 */
	public String getRestfulSign() {
		return restfulSign;
	}

	/**
	 * @param restfulSign the restfulSign to set
	 */
	public void setRestfulSign(String restfulSign) {
		this.restfulSign = restfulSign;
	}
	

}
