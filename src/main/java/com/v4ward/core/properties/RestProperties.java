package com.v4ward.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = RestProperties.REST_PREFIX)
public class RestProperties {
	
	 public static final String REST_PREFIX = "rest";
	 
	 private String coreOpen = "true";
	 
	 
	public static String getRestPrefix() {
		return REST_PREFIX;
	}


	/**
	 * @return the coreOpen
	 */
	public String getCoreOpen() {
		return coreOpen;
	}


	/**
	 * @param coreOpen the coreOpen to set
	 */
	public void setCoreOpen(String coreOpen) {
		this.coreOpen = coreOpen;
	}

	

	

	
	 
	 

}
