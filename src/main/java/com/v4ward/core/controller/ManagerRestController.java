package com.v4ward.core.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.v4ward.core.common.RestConstant;
import com.v4ward.core.model.Manager;
import com.v4ward.core.model.RestParams;
import com.v4ward.core.model.RestUser;
import com.v4ward.core.service.LoginSrv;
import com.v4ward.core.service.ManagerSrv;
import com.v4ward.core.utils.Md5Utils;
import com.v4ward.core.utils.jwt.JwtProperties;
import com.v4ward.core.utils.jwt.JwtTokenUtil;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@RequestMapping("/rest/login")
public class ManagerRestController extends BaseController {

	@Autowired
	private JwtProperties jwtProperties;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private LoginSrv loginSrv;

	@Autowired
	private ManagerSrv managerSrv;

	@ResponseBody
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ApiOperation(value = "移动端登陆", notes = "移动端用户登录")
	@ApiImplicitParam(name = "request", value = "HttpServletRequest", required = true, dataType = "HttpServletRequest")
	public Object loginVali(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String mobile = super.getPara("mobile").trim();
		String password = super.getPara("password").trim();

		Manager manager = this.loginSrv.getManager(mobile);
		if (manager != null) {
			Boolean logFlag = checkPwd(manager, password);
			if (logFlag) {
				String managerId = manager.getId();
				List<String> types = this.loginSrv.getRoleTypeList(managerId);
				String roleStr = "";
				if (types.contains("1") || types.contains("2")) {
					roleStr = "1";
				}
				// 用userId生成token
				String token = jwtTokenUtil.doGenerateToken(managerId);
				RestUser restUser = RestUser.newInstance(token, manager.getName(), manager.getPosition(), roleStr,
						manager.getImName(), manager.getImToken(), manager.getUrl());
				return RestParams.newInstance(RestConstant.SUCCESS_CODE, RestConstant.LOGIN_SUCCESS_DESC, restUser);
			} else {
				return RestParams.newInstance(RestConstant.Failure_CODE, RestConstant.LOGIN_FAIL_WRONG_PASSWORD_DESC,
						null);
			}
		} else {
			return RestParams.newInstance(RestConstant.Failure_CODE, RestConstant.LOGIN_FAIL_NOT_FOUND_MOBILE_DESC,
					null);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/autologin", method = RequestMethod.POST)
	@ApiOperation(value = "移动端自动登陆", notes = "移动端用户自动登录")
	@ApiImplicitParam(name = "request", value = "HttpServletRequest", required = true, dataType = "HttpServletRequest")
	public Object autoLogin(HttpServletRequest request) {

		String token = request.getHeader(jwtProperties.getTokenHeader());
		String userId = jwtTokenUtil.getUsernameFromToken(token);
		Manager user = this.managerSrv.getByUserId(userId);

		List<String> types = this.loginSrv.getRoleTypeList(userId);
		String roleStr = "";
		if (types.contains("1") || types.contains("2")) {
			roleStr = "1";
		}
		// String[] roleType=types.toArray(new String[types.size()]);

		RestUser restUser = RestUser.newInstance(token, user.getName(), user.getPosition(), roleStr, user.getImName(),
				user.getImToken(), user.getUrl());

		return RestParams.newInstance(RestConstant.SUCCESS_CODE, RestConstant.LOGIN_SUCCESS_DESC, restUser);
	}

	@ResponseBody
	@RequestMapping(value = "/updatepwd", method = RequestMethod.POST)
	@ApiOperation(value = "修改密码", notes = "修改密码", response = Map.class, httpMethod = "POST")
	@ApiImplicitParam(paramType = "header", name = "api_version", defaultValue = "v1", required = true, dataType = "String")
	public Object updatepwd(HttpServletRequest request,
			@ApiParam(value = "手机号", required = true) @RequestParam(value = "mobile", required = true) String mobile,
			@ApiParam(value = "旧密码", required = true) @RequestParam(value = "oldpwd", required = true) String oldpwd,
			@ApiParam(value = "新密码", required = true) @RequestParam(value = "newpwd", required = true) String newpwd) {
		String token = request.getHeader(jwtProperties.getTokenHeader());
		String userId = jwtTokenUtil.getUsernameFromToken(token);
		Manager manager = managerSrv.getByUserId(userId);

		String oldMd5 = Md5Utils.encrypt(oldpwd, manager.getSalt());
		if (oldMd5.equals(manager.getPassword())) {
			String newSalt = Md5Utils.getSalt();
			String newMd5 = Md5Utils.encrypt(newpwd, newSalt);

			manager.setSalt(newSalt);
			manager.setPassword(newMd5);

			managerSrv.updateManagerPwd(manager);
			return RestParams.newInstance(RestConstant.SUCCESS_CODE, RestConstant.UPDATEPWD_SUCCESS_DESC, "");
		} else {
			return RestParams.newInstance(RestConstant.Failure_CODE, "原密码错误", "");
		}
	}

	public Boolean checkPwd(Manager manager, String password) {
		String salt = manager.getSalt();
		String dbPwd = manager.getPassword();
		if (dbPwd.equals(Md5Utils.encrypt(password, salt))) {
			return true;
		}
		return false;
	}
}
