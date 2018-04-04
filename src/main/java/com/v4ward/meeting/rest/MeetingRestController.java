package com.v4ward.meeting.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.v4ward.core.utils.CommUtil;
import com.v4ward.core.utils.jwt.JwtProperties;
import com.v4ward.core.utils.jwt.JwtTokenUtil;
import com.v4ward.meeting.service.MeetingRestSrv;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@SuppressWarnings("rawtypes")
@Api(description = "会议信息")
@Controller
@RequestMapping(value = "/rest/meeting", headers = "api_version=v1")
public class MeetingRestController {

	private final static Logger logger = LoggerFactory.getLogger(MeetingRestController.class);

	@Resource
	MeetingRestSrv mRestSrv;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtProperties jwtProperties;

	@RequestMapping(value = "/agenda", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "会议议程", notes = "会议议程", response = Map.class, httpMethod = "POST")
	@ApiImplicitParam(paramType = "header", name = "api_version", defaultValue = "v1", required = true, dataType = "String")
	public Map agenda(
			@ApiParam(value = "会议Id", required = true) @RequestParam(value = "partmeetingId", required = true) String partmeetingId) {
		Map<String, Object> result = new HashMap<>();
		try {
			result = mRestSrv.agenda(partmeetingId);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return CommUtil.setMessage(CommUtil.FAIL, "查询会议议程失败", "");
		}

		return result;
	}

	@RequestMapping(value = "/notice", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "会议须知", notes = "会议须知", response = Map.class, httpMethod = "POST")
	@ApiImplicitParam(paramType = "header", name = "api_version", defaultValue = "v1", required = true, dataType = "String")
	public Map notice(
			@ApiParam(value = "会议Id", required = true) @RequestParam(value = "partmeetingId", required = true) String partmeetingId) {
		Map<String, Object> result = new HashMap<>();
		try {
			result = mRestSrv.notice(partmeetingId);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return CommUtil.setMessage(CommUtil.FAIL, "查询会议须知失败", "");
		}

		return result;
	}

	@RequestMapping(value = "/material", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "会议材料", notes = "会议材料", response = Map.class, httpMethod = "POST")
	@ApiImplicitParam(paramType = "header", name = "api_version", defaultValue = "v1", required = true, dataType = "String")
	public Map material(
			@ApiParam(value = "会议Id", required = true) @RequestParam(value = "partmeetingId", required = true) String partmeetingId) {
		Map<String, Object> result = new HashMap<>();
		try {
			result = mRestSrv.material(partmeetingId);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return CommUtil.setMessage(CommUtil.FAIL, "查询会议材料失败", "");
		}

		return result;
	}

	@RequestMapping(value = "/attendanceInfo", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "参会人员", notes = "会议材料", response = Map.class, httpMethod = "POST")
	@ApiImplicitParam(paramType = "header", name = "api_version", defaultValue = "v1", required = true, dataType = "String")
	public Map attendanceInfo(
			@ApiParam(value = "会议Id", required = true) @RequestParam(value = "partmeetingId", required = true) String partmeetingId,
			@ApiParam(value = "序列号", required = true) @RequestParam(value = "lineNo", required = true) int lineNo) {
		Map<String, Object> result = new HashMap<>();
		try {
			result = mRestSrv.attendanceInfo(partmeetingId, lineNo);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return CommUtil.setMessage(CommUtil.FAIL, "查询参会人员失败", "");
		}

		return result;
	}

	@RequestMapping(value = "/selectCurrent", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "当前会议", notes = "当前会议", response = Map.class, httpMethod = "POST")
	@ApiImplicitParam(paramType = "header", name = "api_version", defaultValue = "v1", required = true, dataType = "String")
	public Map selectCurrent(HttpServletRequest request,
			@ApiParam(value = "序列号", required = true) @RequestParam(value = "lineNo", required = true) int lineNo) {
		Map<String, Object> result = new HashMap<>();
		try {
			String token = request.getHeader(jwtProperties.getTokenHeader());
			String userId = jwtTokenUtil.getUsernameFromToken(token);
			result = mRestSrv.selectCurrent(userId, lineNo);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return CommUtil.setMessage(CommUtil.FAIL, "查询当前会议失败", "");
		}

		return result;
	}

	@RequestMapping(value = "/selectHistory", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "历史会议", notes = "历史会议", response = Map.class, httpMethod = "POST")
	@ApiImplicitParam(paramType = "header", name = "api_version", defaultValue = "v1", required = true, dataType = "String")
	public Map selectHistory(HttpServletRequest request,
			@ApiParam(value = "序列号", required = true) @RequestParam(value = "lineNo", required = true) int lineNo) {
		Map<String, Object> result = new HashMap<>();
		try {
			String token = request.getHeader(jwtProperties.getTokenHeader());
			String userId = jwtTokenUtil.getUsernameFromToken(token);
			result = mRestSrv.selectHistory(userId, lineNo);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return CommUtil.setMessage(CommUtil.FAIL, "查询历史会议失败", "");
		}

		return result;
	}

	@RequestMapping(value = "/selectUserInfo", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "获取用户信息", notes = "获取用户信息", response = Map.class, httpMethod = "POST")
	@ApiImplicitParam(paramType = "header", name = "api_version", defaultValue = "v1", required = true, dataType = "String")
	public Map selectUserInfo(HttpServletRequest request,
			@ApiParam(value = "云信Id", required = true) @RequestParam(value = "idsArr", required = true) String idsArr) {
		Map<String, Object> result = new HashMap<>();
		try {
			List<Map> list = JSON.parseArray(idsArr, Map.class);
			result = mRestSrv.selectUserInfo(list);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return CommUtil.setMessage(CommUtil.FAIL, "查询用户信息失败", "");
		}

		return result;
	}

}
