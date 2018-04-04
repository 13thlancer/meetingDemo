package com.v4ward.core.utils.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * jwt相关配置
 *
 * 
 * @date 2017-08-23 9:23
 */
@Configuration
@ConfigurationProperties(prefix = JwtProperties.JWT_PREFIX)
public class JwtProperties {

	public static final String JWT_PREFIX = "jwt";

	private String tokenHeader = "token";

	private String clientHeader = "client";

	private String secret = "defaultSecret";

	private Long expiration = 180000L;

	private String authPath = "/login/login";

	private String timePath = "time.html";

	private String md5Key = "randomKey";

	public static String getJwtPrefix() {
		return JWT_PREFIX;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public Long getExpiration() {
		return expiration;
	}

	public void setExpiration(Long expiration) {
		this.expiration = expiration;
	}

	public String getAuthPath() {
		return authPath;
	}

	public void setAuthPath(String authPath) {
		this.authPath = authPath;
	}

	public String getMd5Key() {
		return md5Key;
	}

	public void setMd5Key(String md5Key) {
		this.md5Key = md5Key;
	}

	/**
	 * @return the timePath
	 */
	public String getTimePath() {
		return timePath;
	}

	/**
	 * @param timePath
	 *            the timePath to set
	 */
	public void setTimePath(String timePath) {
		this.timePath = timePath;
	}

	/**
	 * @return the tokenHeader
	 */
	public String getTokenHeader() {
		return tokenHeader;
	}

	/**
	 * @param tokenHeader
	 *            the tokenHeader to set
	 */
	public void setTokenHeader(String tokenHeader) {
		this.tokenHeader = tokenHeader;
	}

	/**
	 * @return the clientHeader
	 */
	public String getClientHeader() {
		return clientHeader;
	}

	/**
	 * @param clientHeader the clientHeader to set
	 */
	public void setClientHeader(String clientHeader) {
		this.clientHeader = clientHeader;
	}

	

}
