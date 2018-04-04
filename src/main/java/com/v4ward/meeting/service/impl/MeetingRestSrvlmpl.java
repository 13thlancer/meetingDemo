/**   
 * Copyright © 2018 武汉现代物华科技发展有限公司信息分公司. All rights reserved.
 * 
 * @Title: MeetingSrvImpl.java 
 * @Prject: wisdri-meeting
 * @Package: com.v4ward.meeting.service.impl
 * @author: 彭浩
 * @date: 2018年3月5日 下午2:15:05 
 * @version: V1.0   
 */
package com.v4ward.meeting.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.v4ward.core.utils.CommUtil;
import com.v4ward.core.utils.DateUtil;
import com.v4ward.meeting.dao.MeetingRestMapper;
import com.v4ward.meeting.dao.SignRestMapper;
import com.v4ward.meeting.model.SysMeeting;
import com.v4ward.meeting.model.SysPartmeetingUser;
import com.v4ward.meeting.model.SysResources;
import com.v4ward.meeting.model.SysSubmeeting;
import com.v4ward.meeting.service.MeetingRestSrv;

/**
 * 会议管理service
 * 
 * @ClassName: MeetingSrvImpl
 * @author: 彭浩
 * @date: 2018年3月5日 下午2:15:05
 */
@Service
@Transactional
public class MeetingRestSrvlmpl implements MeetingRestSrv {

	private final static Logger logger = LoggerFactory.getLogger(MeetingRestSrvlmpl.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	SignRestMapper signRestMapper;

	@Autowired
	MeetingRestMapper meetingRestMapper;

	@Override
	public Map<String, Object> agenda(String partmeetingId) throws Exception {
		List<SysSubmeeting> ls = meetingRestMapper.selectSysSubmeetingByPartmeetingId(partmeetingId);
		if (CollectionUtils.isNotEmpty(ls)) {
			List<Map<String, String>> lsRecord = new ArrayList<>();
			for (SysSubmeeting submeeting : ls) {
				Map<String, String> map = new HashMap<>();
				map.put("subject", submeeting.getSubmeetingTheme());
				map.put("time", DateUtil.format(submeeting.getStarttime(), "yyyy-MM-dd HH:mm:ss"));
				map.put("location", submeeting.getSubmeetingLocation());
				map.put("compere", submeeting.getSubmeetingHost());
				lsRecord.add(map);
			}
			return CommUtil.setMessage(CommUtil.SUCCESS, "查询成功", lsRecord);
		} else {
			return CommUtil.setMessage(CommUtil.SUCCESS, "未查询到会议议程", "");
		}
	}

	@Override
	public Map<String, Object> notice(String partmeetingId) throws Exception {
		SysMeeting meeting = meetingRestMapper.selectMeetingByPartmeetingId(partmeetingId);
		if (meeting != null) {
			Map<String, String> map = new HashMap<>();
			map.put("notice", meeting.getMeetingNotice());
			return CommUtil.setMessage(CommUtil.SUCCESS, "查询成功", map);
		} else {
			return CommUtil.setMessage(CommUtil.SUCCESS, "未查询到会议须知", "");
		}
	}

	@Override
	public Map<String, Object> material(String partmeetingId) throws Exception {
		List<SysResources> ls = meetingRestMapper.selectSysResourcesByPartmeetingId(partmeetingId);
		if (CollectionUtils.isNotEmpty(ls)) {
			List<Map<String, String>> lsRecord = new ArrayList<>();
			for (SysResources resources : ls) {
				Map<String, String> map = new HashMap<>();
				map.put("title", resources.getResourcesTitle());
				map.put("url", resources.getResourcesPath());
				map.put("size", this.formetFileSize(resources.getResourcesSize().longValue()));
				if (resources.getResourcesStatus() == 0) {
					map.put("signcan", "1");
				}
				if (resources.getResourcesStatus() == 1) {
					map.put("signcan", "0");
				}
				lsRecord.add(map);
			}
			return CommUtil.setMessage(CommUtil.SUCCESS, "查询成功", lsRecord);
		} else {
			return CommUtil.setMessage(CommUtil.SUCCESS, "未查询到会议材料", "");
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public Map<String, Object> attendanceInfo(String partmeetingId, int lineNo) throws Exception {
		try {
			Map<String, Object> param = new HashMap<>();
			param.put("partmeetingId", partmeetingId);
			param.put("lineNo", lineNo);
			List<SysPartmeetingUser> ls = meetingRestMapper.selectSysPartmeetingUser(param);
			if (CollectionUtils.isNotEmpty(ls)) {
				List<Map<String, String>> lsRecord = new ArrayList<>();
				for (SysPartmeetingUser partmeetingUser : ls) {
					Map<String, String> map = new HashMap<>();
					String userId = partmeetingUser.getUserId();
					Map<String, Object> userMap = this.getUserInfo(userId);
					if (MapUtils.isNotEmpty(userMap)) {
						map.put("name", ObjectUtils.toString(userMap.get("name")));
						map.put("accid", ObjectUtils.toString(userMap.get("accid")));
					} else {
						map.put("name", "");
						map.put("accid", "");
					}
					Map<String, Object> orgMap = this.getOrgInfoByUser(userId);
					if (MapUtils.isNotEmpty(orgMap)) {
						map.put("org", ObjectUtils.toString(orgMap.get("fullNames")));
					} else {
						map.put("org", "");
					}
					map.put("roomNo", partmeetingUser.getRoomInfo() != null ? partmeetingUser.getRoomInfo() : "");
					map.put("lineNo", partmeetingUser.getLineNum() + "");
					lsRecord.add(map);
				}
				return CommUtil.setMessage(CommUtil.SUCCESS, "查询成功", lsRecord);
			} else {
				return CommUtil.setMessage(CommUtil.SUCCESS, "未查询到参会人员", "");
			}
		} catch (Exception e) {
			logger.error("查询参会人员失败" + e.getMessage());
			return CommUtil.setMessage(CommUtil.FAIL, "查询失败", "");
		}

	}

	@Override
	public Map<String, Object> selectCurrent(String userId, int lineNo) throws Exception {
		Date currentTime = new Date();
		Map<String, Object> param = new HashMap<>();
		param.put("userId", userId);
		param.put("lineNo", lineNo);
		param.put("currentTime", DateUtil.format(currentTime, "yyyy-MM-dd HH:mm:ss"));
		List<Map<String, Object>> ls = meetingRestMapper.selectCurrentPartmeeting(param);
		if (CollectionUtils.isNotEmpty(ls)) {
			return CommUtil.setMessage(CommUtil.SUCCESS, "查询成功", ls);
		} else {
			return CommUtil.setMessage(CommUtil.SUCCESS, "查无记录", "");
		}
	}

	@Override
	public Map<String, Object> selectHistory(String userId, int lineNo) throws Exception {
		Date currentTime = new Date();
		Map<String, Object> param = new HashMap<>();
		param.put("userId", userId);
		param.put("lineNo", lineNo);
		param.put("currentTime", DateUtil.format(currentTime, "yyyy-MM-dd HH:mm:ss"));
		List<Map<String, Object>> ls = meetingRestMapper.selectHistoryPartmeeting(param);
		if (CollectionUtils.isNotEmpty(ls)) {
			return CommUtil.setMessage(CommUtil.SUCCESS, "查询成功", ls);
		} else {
			return CommUtil.setMessage(CommUtil.SUCCESS, "查无记录", "");
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, Object> selectUserInfo(List<Map> idsArr) throws Exception {
		if (CollectionUtils.isEmpty(idsArr)) {
			return CommUtil.setMessage(CommUtil.FAIL, "接口参数不能为空", "");
		}
		List<Map<String, Object>> ls = this.getUserInfo(idsArr);
		if (CollectionUtils.isNotEmpty(ls)) {
			return CommUtil.setMessage(CommUtil.SUCCESS, "查询成功", ls);
		} else {
			return CommUtil.setMessage(CommUtil.SUCCESS, "查无记录", "");
		}
	}

	@SuppressWarnings("rawtypes")
	public List<Map<String, Object>> getUserInfo(List<Map> accidList) {
		StringBuffer sql = new StringBuffer(
				"SELECT T.NAME_ name,T.IMNAME_ accid,T.ID_ userId FROM TB_MANAGER T WHERE T.IMNAME_ IN (");
		if (CollectionUtils.isNotEmpty(accidList)) {
			for (Map map : accidList) {
				sql.append("'" + map.get("accid") + "',");
			}
			return jdbcTemplate.queryForList(sql.substring(0, sql.length() - 1) + ")");
		} else {
			return null;
		}
	}

	public Map<String, Object> getUserInfo(String userId) {
		String sql = "SELECT T.NAME_ name,T.IMNAME_ accid,T.ID_ userId FROM TB_MANAGER T WHERE T.ID_ = ?";
		List<Map<String, Object>> ls = jdbcTemplate.queryForList(sql, userId);
		if (CollectionUtils.isNotEmpty(ls)) {
			return ls.get(0);
		}
		return null;
	}

	public Map<String, Object> getOrgInfoByUser(String userId) {
		String sql = "SELECT B.FULLNAMES_ fullNames FROM TB_MANAGER_ORG A,TB_ORG B "
				+ "WHERE A.ORGID_ = B.ID_ AND A.MANAGERID_ = ?";
		List<Map<String, Object>> ls = jdbcTemplate.queryForList(sql, userId);
		if (CollectionUtils.isNotEmpty(ls)) {
			return ls.get(0);
		}
		return null;
	}

	public String formetFileSize(long fileS) {// 转换文件大小
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "KB";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "MB";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "GB";
		}
		return fileSizeString;
	}
}
