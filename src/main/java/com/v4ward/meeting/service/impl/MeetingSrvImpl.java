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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.v4ward.core.utils.UUIDUtil;
import com.v4ward.meeting.dao.MeetingMapper;
import com.v4ward.meeting.model.SysMeeting;
import com.v4ward.meeting.model.SysMeetingUser;
import com.v4ward.meeting.service.MeetingSrv;

import cn.hutool.core.collection.CollectionUtil;

/** 
 * 会议管理service
 * @ClassName: MeetingSrvImpl
 * @author: 彭浩
 * @date: 2018年3月5日 下午2:15:05  
 */
@Service
@Transactional
public class MeetingSrvImpl implements MeetingSrv {
	
	@Autowired
	MeetingMapper meetingMapper;

	/** 
	 * (non Javadoc) 
	 * @Title: selectMeetingById
	 * @param id
	 * @return
	 * @see com.v4ward.meeting.service.MeetingSrv#selectMeetingById(java.lang.String)
	 */
	@Override
	public SysMeeting selectMeetingById(String id) {

		return meetingMapper.selectMeetingById(id);
	}

	/** 
	 * (non Javadoc) 
	 * @Title: addMeeting
	 * @param meeting
	 * @see com.v4ward.meeting.service.MeetingSrv#addMeeting(com.v4ward.meeting.model.SysMeeting)
	 */
	@Override
	public void addMeeting(SysMeeting meeting) {
		
		meeting.setId(UUIDUtil.getUUID());
		meeting.setDeleteStatus(0);
		meeting.setVersion(0);
		meetingMapper.insert(meeting);
	}

	/** 
	 * (non Javadoc) 
	 * @Title: list
	 * @param params
	 * @return
	 * @see com.v4ward.meeting.service.MeetingSrv#listMeetings(java.util.Map)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<Map<String, Object>> listMeetings(Map params) {
		
		return meetingMapper.listMeetings(params);
	}
	
	/** 
	 * (non Javadoc) 
	 * @Title: list
	 * @param params
	 * @return
	 * @see com.v4ward.meeting.service.MeetingSrv#listMeetingsByRoleType(java.util.Map)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<Map<String, Object>> listMeetingsByRoleType(Map params) {
		
		return meetingMapper.listMeetingsByRoleType(params);
	}

	/** 
	 * (non Javadoc) 
	 * @Title: editMeeting
	 * @param meeting
	 * @see com.v4ward.meeting.service.MeetingSrv#editMeeting(com.v4ward.meeting.model.SysMeeting)
	 */
	@Override
	public void editMeeting(SysMeeting meeting) {
		
		meetingMapper.edit(meeting);
	}

	/** 
	 * (non Javadoc) 
	 * @Title: deleteMeeting
	 * @param id
	 * @see com.v4ward.meeting.service.MeetingSrv#deleteMeeting(java.lang.String)
	 */
	@Override
	public void deleteMeeting(String id) {
		meetingMapper.deleteById(id);
	}

	/** 
	 * (non Javadoc) 
	 * @Title: getSuperRoleOrgId
	 * @param managerId
	 * @return
	 * @see com.v4ward.meeting.service.MeetingSrv#getSuperRoleOrgId(java.lang.String)
	 */
	@Override
	public String getSuperRoleOrgId(String managerId) {
		
		return meetingMapper.getSuperRoleOrgId(managerId);
	}
	
	/** 
     * 获取会务管理员列表
     * @Title: getMeetingManager
     * @param meetingId
     * @return
     * @return: List<Map<String,Object>>
     */
	@Override
	public List<Map<String, Object>> getMeetingManager(String meetingId) {
		return meetingMapper.getMeetingManager(meetingId);
	}

	/**
	 * 分配新的会务管理员 ，先清空，再新增 
	 * @Title: allotManager
	 * @param meetingId
	 * @param managerIdList
	 * @see com.v4ward.meeting.service.MeetingSrv#allotManager(java.lang.String, java.util.List)
	 */
	@Override
	public void allotManager(String meetingId, List<String> managerIdList) {
		
		this.meetingMapper.deleteMeetingUserByMeetingId(meetingId);
		
		if(CollectionUtil.isEmpty(managerIdList)){
			return;
		}
		List<SysMeetingUser> list = new ArrayList<SysMeetingUser>();
		SysMeetingUser user = null;
		for (String managerId : managerIdList) {
			user = new SysMeetingUser();
			user.setId(UUIDUtil.getUUID());
			user.setMeetingId(meetingId);
			user.setUserId(managerId);
			user.setVersion(0);
			list.add(user);
		}
		
		meetingMapper.insertMeetingUserList(list);
		
	}

}
