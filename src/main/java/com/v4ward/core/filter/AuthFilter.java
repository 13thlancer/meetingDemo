package com.v4ward.core.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.OncePerRequestFilter;

import com.v4ward.core.common.RestConstant;
import com.v4ward.core.model.Manager;
import com.v4ward.core.model.Menu;
import com.v4ward.core.model.RestParams;
import com.v4ward.core.properties.V4wardProperties;
import com.v4ward.core.utils.jwt.JwtProperties;
import com.v4ward.core.utils.jwt.JwtTokenUtil;
import com.v4ward.core.utils.jwt.RenderUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

public class AuthFilter extends OncePerRequestFilter {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtProperties jwtProperties;

	/*@Value("${restful.sign}")
	private String restfulSign;*/
	@Autowired
	private V4wardProperties v4wardProperties;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String servletPath = request.getServletPath();
		if (servletPath.indexOf(v4wardProperties.getRestfulSign()) >= 0) {

			if (servletPath.indexOf(jwtProperties.getAuthPath()) < 0) {
				final String requestHeader = request.getHeader(jwtProperties.getTokenHeader());
				String authToken = null;
				if (requestHeader != null && !requestHeader.equals("")) {
					authToken = requestHeader;

					// 验证token是否过期
					try {
						jwtTokenUtil.isTokenExpired(authToken);// 同时会校验token是否正确，如不正确就会抛异常

						/*
						 * boolean flag =
						 * jwtTokenUtil.isTokenExpired(authToken);//
						 * 同时会校验token是否正确，如不正确就会抛异常 if (flag) {
						 * RenderUtil.renderJson(response,
						 * RestParams.newInstance(RestConstant.
						 * UPLOAD_IMG_ERROR_CODE,RestConstant.
						 * UPLOAD_IMG_ERROR_DESC,null)); return; }
						 */
					} catch (ExpiredJwtException e) {
						// 过期直接抛异常

						logger.error("token超时异常:" + "_" + e.getMessage(), e);
						RenderUtil.renderJson(response, RestParams.newInstance(RestConstant.TOKEN_TIME_EXPIRED_CODE,
								RestConstant.TOKEN_TIME_EXPIRED_DESC, null));
						return;
					} catch (JwtException e) {
						// 有异常就是token解析失败

						logger.error("token解析异常:" + "_" + e.getMessage(), e);
						RenderUtil.renderJson(response, RestParams.newInstance(RestConstant.TOKEN_NOT_MATCH_CODE,
								RestConstant.TOKEN_NOT_MATCH_DESC, null));
						return;
					}
				} else {
					// header没有token
					RenderUtil.renderJson(response, RestParams.newInstance(RestConstant.TOKEN_NOT_EXISTS_CODE,
							RestConstant.TOKEN_NOT_EXISTS_DESC, null));
					return;
				}
			}
		} else {
			if (!doFilterResourse(servletPath)) {
				if (!servletPath.equals("/login") && !servletPath.equals("/register") && !servletPath.equals("/logout")
						&& !servletPath.equals("/defaultKaptcha")
						&& !servletPath.equals("/i18n/changeSessionLanguage")) {// 过滤登录和注册请求，以及回调

					if (!getUserSession(request)) {
						// 如果是ajax请求响应头会有，x-requested-with
						if (request.getHeader("x-requested-with") != null
								&& request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
							response.setHeader("sessionstatus", "timeout");// 在响应头设置session状态
						} else {
							response.sendRedirect(request.getContextPath() + "/login");
						}
						return;
					}
				}
			}
		}
		chain.doFilter(request, response);
	}

	/**
	 *
	 * @Title: getUserSession
	 * @param request
	 * @return session有效 true ; 失效 false
	 * @return: boolean
	 */
	boolean getUserSession(HttpServletRequest request) {

		boolean isLogin = false;
		try {
			HttpSession session = request.getSession();
			if (null != session) {
				Manager manager = (Manager) session.getAttribute("manager");
				if (null != manager) {
					isLogin = true;
				}
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
		return isLogin;
	}

	@SuppressWarnings("unchecked")
	public boolean checkAuth(HttpServletRequest request, String url) {
		HttpSession session = request.getSession();
		List<Menu> auths = (List<Menu>) session.getAttribute("auths");
		for (Menu auth : auths) {
			if (url.equals(auth.getUrl())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 过滤静态资源
	 * 
	 * @Title: doFilterResourse
	 * @param servletPath
	 * @return 是：true 不是：false
	 * @return: boolean
	 */
	private boolean doFilterResourse(String servletPath) {

		final String resourseSuffix[] = { ".js", ".css", ".jpg", ".png", ".gif", ".ico", ".pdf" };
		for (String suffix : resourseSuffix) {
			if (servletPath.toLowerCase().endsWith(suffix)) {
				return true;
			}
		}
		return false;
	}


}