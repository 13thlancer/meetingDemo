package com.v4ward.meeting.rest;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.v4ward.core.utils.CommUtil;
import com.v4ward.core.utils.jwt.JwtTokenUtil;
import com.v4ward.meeting.service.SignRestSrv;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@SuppressWarnings("rawtypes")
@Api(description = "签到管理")
@Controller
@RequestMapping(value = "/rest/sign", headers = "api_version=v1")
public class SignRestController {

	private final static Logger logger = LoggerFactory.getLogger(SignRestController.class);

	@Resource
	SignRestSrv sRestSrv;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	// @Autowired
	// private JwtProperties jwtProperties;

	@RequestMapping(value = "/scanOrdinary", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "扫会议码", notes = "扫会议码", response = Map.class, httpMethod = "POST")
	@ApiImplicitParam(paramType = "header", name = "api_version", defaultValue = "v1", required = true, dataType = "String")
	public Map scanOrdinary(
			@ApiParam(value = "会议Id", required = true) @RequestParam(value = "partmeetingId", required = true) String partmeetingId,
			@ApiParam(value = "参会人", required = true) @RequestParam(value = "attendance", required = true) String attendance) {
		Map<String, Object> result = new HashMap<>();
		try {
			String userId = jwtTokenUtil.getUsernameFromToken(attendance);
			result = sRestSrv.signScanOrdinary(partmeetingId, userId);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return CommUtil.setMessage(CommUtil.FAIL, "签到失败", "");
		}

		return result;
	}

	@RequestMapping(value = "/scanSpecial", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "扫个人码", notes = "扫个人码", response = Map.class, httpMethod = "POST")
	@ApiImplicitParam(paramType = "header", name = "api_version", defaultValue = "v1", required = true, dataType = "String")
	public Map scanSpecial(
			@ApiParam(value = "会议Id", required = true) @RequestParam(value = "partmeetingId", required = true) String partmeetingId,
			@ApiParam(value = "参会人", required = true) @RequestParam(value = "attendance", required = true) String attendance,
			@ApiParam(value = "记录人", required = true) @RequestParam(value = "recorder", required = true) String recorder) {
		Map<String, Object> result = new HashMap<>();
		try {
			String attendanceId = jwtTokenUtil.getUsernameFromToken(attendance);
			String recorderId = jwtTokenUtil.getUsernameFromToken(recorder);
			result = sRestSrv.signScanSpecial(partmeetingId, attendanceId, recorderId);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return CommUtil.setMessage(CommUtil.FAIL, "签到失败", "");
		}

		return result;
	}

	@RequestMapping(value = "/statisticsTotal", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "签到统计", notes = "签到统计", response = Map.class, httpMethod = "POST")
	@ApiImplicitParam(paramType = "header", name = "api_version", defaultValue = "v1", required = true, dataType = "String")
	public Map statisticsTotal(
			@ApiParam(value = "会议Id", required = true) @RequestParam(value = "partmeetingId", required = true) String partmeetingId) {
		Map<String, Object> result = new HashMap<>();
		try {
			result = sRestSrv.statisticsTotal(partmeetingId);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return CommUtil.setMessage(CommUtil.FAIL, "签到统计异常", "");
		}
		return result;
	}

	@RequestMapping(value = "/statisticsDetails", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "签到统计", notes = "签到统计", response = Map.class, httpMethod = "POST")
	@ApiImplicitParam(paramType = "header", name = "api_version", defaultValue = "v1", required = true, dataType = "String")
	public Map statisticsDetails(
			@ApiParam(value = "会议Id", required = true) @RequestParam(value = "partmeetingId", required = true) String partmeetingId,
			@ApiParam(value = "签到状态", required = true) @RequestParam(value = "status", required = true) String status) {
		Map<String, Object> result = new HashMap<>();
		try {
			result = sRestSrv.statisticsDetails(partmeetingId, status);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return CommUtil.setMessage(CommUtil.FAIL, "签到统计异常", "");
		}
		return result;
	}

	@RequestMapping(value = "/checksignin", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "是否签到", notes = "是否签到", response = Map.class, httpMethod = "POST")
	@ApiImplicitParam(paramType = "header", name = "api_version", defaultValue = "v1", required = true, dataType = "String")
	public Map checksignin(
			@ApiParam(value = "会议Id", required = true) @RequestParam(value = "partmeetingId", required = true) String partmeetingId,
			@ApiParam(value = "参会人", required = true) @RequestParam(value = "attendance", required = true) String attendance) {
		Map<String, Object> result = new HashMap<>();
		try {
			String userId = jwtTokenUtil.getUsernameFromToken(attendance);
			result = sRestSrv.checksignin(partmeetingId, userId);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return CommUtil.setMessage(CommUtil.FAIL, "签到失败", "");
		}
		return result;
	}

}
