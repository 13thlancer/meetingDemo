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
import com.v4ward.core.utils.UUIDUtil;
import com.v4ward.meeting.dao.PartmeetingMapper;
import com.v4ward.meeting.dao.SignRestMapper;
import com.v4ward.meeting.model.SysPartmeeting;
import com.v4ward.meeting.model.SysPartmeetingUser;
import com.v4ward.meeting.service.SignRestSrv;

/**
 * 会议管理service
 * 
 * @ClassName: MeetingSrvImpl
 * @author: 彭浩
 * @date: 2018年3月5日 下午2:15:05
 */
@Service
@Transactional
public class SignRestSrvlmpl implements SignRestSrv {

	private final static Logger logger = LoggerFactory.getLogger(SignRestSrvlmpl.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	SignRestMapper signRestMapper;

	@Autowired
	PartmeetingMapper partmeetingMapper;

	@Autowired
	private MeetingRestSrvlmpl mrSrvImpl;

	/**
	 * 参会人员扫码签到
	 * 
	 * @Title: signScanOrdinary
	 * @param partmeetingId
	 * @param userId
	 * @return
	 * @throws Exception
	 * @see com.v4ward.meeting.service.SignRestSrv#signScanOrdinary(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public Map<String, Object> signScanOrdinary(String partmeetingId, String userId) throws Exception {
		Date currentDate = new Date();
		Map<String, String> result = new HashMap<>();
		SysPartmeeting partmeeting = partmeetingMapper.selectPartmeetingById(partmeetingId);
		if (partmeeting != null) {
			Date starttime = partmeeting.getStarttime();
			Date endtime = partmeeting.getEndtime();
			Date earlytime = partmeeting.getEarlytime();
			Date latesttime = partmeeting.getLatesttime();
			int scanStatus = partmeeting.getScanStatus();
			int signStatus = 0;
			// if (scanStatus > 0) {
			// return CommUtil.setMessage(CommUtil.FAIL, "不允许扫码签到", "");
			// }
			if (currentDate.before(earlytime)) {
				return CommUtil.setMessage(CommUtil.FAIL, "还未到签到时间", "");
			}
			if (currentDate.after(latesttime)) {
				return CommUtil.setMessage(CommUtil.FAIL, "签到时间已过", "");
			}
			if (currentDate.getTime() > starttime.getTime() && currentDate.getTime() < endtime.getTime()) {
				// 迟到
				signStatus = 2;
			} else {
				// 签到
				signStatus = 1;
			}

			SysPartmeetingUser partmeetingUser = signRestMapper.selectSysPartmeetingUserById(partmeetingId, userId);
			if (partmeetingUser != null) {
				if (partmeetingUser.getSignStatus() != 0) {
					return CommUtil.setMessage(CommUtil.FAIL, "用户已签到，不能重复签到", "");
				}
				partmeetingUser.setUserId(userId);
				partmeetingUser.setPartmeetingId(partmeetingId);
				partmeetingUser.setSignStatus(signStatus);
				partmeetingUser.setSigntime(currentDate);
				signRestMapper.updateSysPartmeetingUser(partmeetingUser);
			} else {

				if (scanStatus > 0) {
					return CommUtil.setMessage(CommUtil.FAIL, "本会议不允许扫码签到", "");
				}

				partmeetingUser = new SysPartmeetingUser();
				partmeetingUser.setId(UUIDUtil.getUUID());
				partmeetingUser.setUserId(userId);
				partmeetingUser.setPartmeetingId(partmeetingId);
				partmeetingUser.setBindStatus(1);
				partmeetingUser.setSignStatus(signStatus);
				partmeetingUser.setSigntime(currentDate);
				partmeetingUser.setLineNum(this.getLineNum(partmeetingId));
				signRestMapper.insertSysPartmeetingUser(partmeetingUser);
			}
			result.put("subject", partmeeting.getPartmeetingTitle());
		} else {
			logger.error("未找到【" + partmeetingId + "】对应的分会场");
			throw new RuntimeException("未找到【" + partmeetingId + "】对应的分会场");
		}
		return CommUtil.setMessage(CommUtil.SUCCESS, "签到成功", result);
	}

	/**
	 * 会务管理员替参会人员扫码签到(参会人员app无法扫码时使用此功能)
	 * 
	 * @Title: signScanSpecial
	 * @param partmeetingId
	 * @param userId
	 * @param recorderId
	 * @return
	 * @throws Exception
	 * @see com.v4ward.meeting.service.SignRestSrv#signScanSpecial(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public Map<String, Object> signScanSpecial(String partmeetingId, String userId, String recorderId)
			throws Exception {
		Date currentDate = new Date();
		Map<String, String> result = new HashMap<>();
		SysPartmeeting partmeeting = partmeetingMapper.selectPartmeetingById(partmeetingId);
		if (partmeeting != null) {
			Date starttime = partmeeting.getStarttime();
			Date endtime = partmeeting.getEndtime();
			Date earlytime = partmeeting.getEarlytime();
			Date latesttime = partmeeting.getLatesttime();
			// int scanStatus = partmeeting.getScanStatus();
			int signStatus = 0;
			// if (scanStatus > 0) {
			// return CommUtil.setMessage(CommUtil.FAIL, "不允许扫码签到", "");
			// }
			if (!this.checkManager(partmeetingId, recorderId)) {
				return CommUtil.setMessage(CommUtil.FAIL, "您不是本会议会务管理员，无签到权限", "");
			}
			if (currentDate.before(earlytime)) {
				return CommUtil.setMessage(CommUtil.FAIL, "还未到签到时间", "");
			}
			if (currentDate.after(latesttime)) {
				return CommUtil.setMessage(CommUtil.FAIL, "签到时间已过", "");
			}
			if (currentDate.getTime() > starttime.getTime() && currentDate.getTime() < endtime.getTime()) {
				// 迟到
				signStatus = 2;
			} else {
				// 签到
				signStatus = 1;
			}

			SysPartmeetingUser partmeetingUser = signRestMapper.selectSysPartmeetingUserById(partmeetingId, userId);
			if (partmeetingUser != null) {
				if (partmeetingUser.getSignStatus() != 0) {
					return CommUtil.setMessage(CommUtil.FAIL, "用户已签到，不能重复签到", "");
				}
				partmeetingUser.setUserId(userId);
				partmeetingUser.setManagerId(recorderId);
				partmeetingUser.setPartmeetingId(partmeetingId);
				partmeetingUser.setSignStatus(signStatus);
				partmeetingUser.setSigntime(currentDate);

				signRestMapper.updateSysPartmeetingUser(partmeetingUser);
			} else {
				partmeetingUser = new SysPartmeetingUser();
				partmeetingUser.setId(UUIDUtil.getUUID());
				partmeetingUser.setUserId(userId);
				partmeetingUser.setManagerId(recorderId);
				partmeetingUser.setPartmeetingId(partmeetingId);
				partmeetingUser.setBindStatus(1);
				partmeetingUser.setSignStatus(signStatus);
				partmeetingUser.setSigntime(currentDate);
				partmeetingUser.setLineNum(this.getLineNum(partmeetingId));
				signRestMapper.insertSysPartmeetingUser(partmeetingUser);
			}
			result.put("subject", partmeeting.getPartmeetingTitle());
			Map<String, Object> userMap = mrSrvImpl.getUserInfo(userId);
			if (MapUtils.isNotEmpty(userMap)) {
				result.put("attName", ObjectUtils.toString(userMap.get("name")));
			} else {
				result.put("attName", "");
			}
		} else {
			logger.error("未找到【" + partmeetingId + "】对应的分会场");
			throw new RuntimeException("未找到【" + partmeetingId + "】对应的分会场");
		}
		return CommUtil.setMessage(CommUtil.SUCCESS, "签到成功", result);
	}

	@Override
	public Map<String, Object> checksignin(String partmeetingId, String userId) throws Exception {
		SysPartmeetingUser partmeetingUser = signRestMapper.selectSysPartmeetingUserById(partmeetingId, userId);
		if (partmeetingUser != null) {
			switch (partmeetingUser.getSignStatus()) {
			case 0:
				return CommUtil.setMessage(CommUtil.FAIL, "未到签", "");
			case 1:
				return CommUtil.setMessage(CommUtil.SUCCESS, "已签到", "");
			case 2:
				return CommUtil.setMessage(CommUtil.SUCCESS, "迟到", "");
			case 3:
				return CommUtil.setMessage(CommUtil.FAIL, "请假", "");
			default:
				break;
			}
		}
		return CommUtil.setMessage(CommUtil.FAIL, "签到记录异常", "");
	}

	@SuppressWarnings("deprecation")
	public int getLineNum(String partmeetingId) throws Exception {
		String sql = "SELECT IFNULL(MAX(T.LINE_NUM_),0)+1 num  FROM SYS_PARTMEETING_USER T WHERE T.PARTMEETING_ID_ = ?";
		Map<String, Object> map = jdbcTemplate.queryForMap(sql, partmeetingId);
		if (MapUtils.isNotEmpty(map)) {
			return Integer.valueOf(ObjectUtils.toString(map.get("num")));
		}
		return 0;
	}

	@Override
	public Map<String, Object> statisticsTotal(String partmeetingId) throws Exception {
		Map<String, Object> mapTotal = this.statisticsPartmeetingUser(partmeetingId);
		return CommUtil.setMessage(CommUtil.SUCCESS, "查询成功", mapTotal);
	}

	@Override
	public Map<String, Object> statisticsDetails(String partmeetingId, String status) throws Exception {
		Map<String, Object> param = new HashMap<>();
		param.put("partmeetingId", partmeetingId);
		param.put("signStatus", status);
		List<Map<String, Object>> ls = signRestMapper.selectSysPartmeetingUser(param);
		if (CollectionUtils.isNotEmpty(ls)) {
			for (Map<String, Object> map : ls) {
				if (map.get("name") == null) {
					map.put("name", "");
				}
				if (map.get("org") == null) {
					map.put("org", "");
				}
				if (map.get("time") == null) {
					map.put("time", "");
				}
			}
		}

		return CommUtil.setMessage(CommUtil.SUCCESS, "查询成功", ls);
	}

	public Map<String, Object> statisticsPartmeetingUser(String partmeetingId) {
		String sql = "SELECT DISTINCT (SELECT COUNT(T.SIGN_STATUS_) FROM SYS_PARTMEETING_USER T WHERE PARTMEETING_ID_ = ?) total,"
				+ "(SELECT COUNT(T.SIGN_STATUS_) FROM SYS_PARTMEETING_USER T WHERE PARTMEETING_ID_ = ? AND T.SIGN_STATUS_ = '1') signin,"
				+ "(SELECT COUNT(T.SIGN_STATUS_) FROM SYS_PARTMEETING_USER T WHERE PARTMEETING_ID_ = ? AND T.SIGN_STATUS_ = '0') notsignin,"
				+ "(SELECT COUNT(T.SIGN_STATUS_) FROM SYS_PARTMEETING_USER T WHERE PARTMEETING_ID_ = ? AND T.SIGN_STATUS_ = '2') late,"
				+ "(SELECT COUNT(T.SIGN_STATUS_) FROM SYS_PARTMEETING_USER T WHERE PARTMEETING_ID_ = ? AND T.SIGN_STATUS_ = '3') absence "
				+ "FROM SYS_PARTMEETING_USER WHERE PARTMEETING_ID_ = ?";
		return jdbcTemplate.queryForMap(sql, new Object[] { partmeetingId, partmeetingId, partmeetingId, partmeetingId,
				partmeetingId, partmeetingId });
	}

	public boolean checkManager(String partmeetingId, String managerId) {
		String sql = "SELECT A.MEETING_ID_,A.MEETING_ID_ FROM sys_meeting_user A,sys_partmeeting B "
				+ "WHERE A.MEETING_ID_ = B.MEETING_ID_ AND B.ID_ = ? AND A.USER_ID_ = ?";
		List<Map<String, Object>> ls = jdbcTemplate.queryForList(sql, new Object[] { partmeetingId, managerId });
		return CollectionUtils.isNotEmpty(ls);
	}

}
