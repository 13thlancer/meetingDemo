/**   
 * Copyright © 2018 武汉现代物华科技发展有限公司信息分公司. All rights reserved.
 * 
 * @Title: PartmeetingSrvImpl.java 
 * @Prject: wisdri-meeting
 * @Package: com.v4ward.meeting.service.impl
 * @author: 彭浩
 * @date: 2018年3月8日 下午4:33:18 
 * @version: V1.0   
 */
package com.v4ward.meeting.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.v4ward.core.utils.UUIDUtil;
import com.v4ward.meeting.dao.PartmeetingMapper;
import com.v4ward.meeting.dao.PartmeetingUserMapper;
import com.v4ward.meeting.dao.ResourcesMapper;
import com.v4ward.meeting.model.SysPartmeeting;
import com.v4ward.meeting.model.SysPartmeetingUser;
import com.v4ward.meeting.model.SysResources;
import com.v4ward.meeting.service.PartmeetingSrv;

/** 
 * 分会场管理serviceImpl
 * @ClassName: PartmeetingSrvImpl
 * @author: 彭浩
 * @date: 2018年3月8日 下午4:33:18  
 */
@Service
@Transactional
public class PartmeetingSrvImpl implements PartmeetingSrv {

	@Autowired
	PartmeetingMapper partmeetingMapper;
	@Autowired
	ResourcesMapper resourcesMapper;
	@Autowired
	PartmeetingUserMapper partmeetingUserMapper;
	
	/** 
	 * (non Javadoc) 
	 * @Title: selectPartmeetingById
	 * @param id
	 * @return
	 * @see com.v4ward.meeting.service.PartmeetingSrv#selectPartmeetingById(java.lang.String)
	 */
	@Override
	public SysPartmeeting selectPartmeetingById(String id) {

		return partmeetingMapper.selectPartmeetingById(id);
	}

	/** 
	 * (non Javadoc) 
	 * @Title: addPartmeeting
	 * @param partmeeting
	 * @see com.v4ward.meeting.service.PartmeetingSrv#addPartmeeting(com.v4ward.meeting.model.SysPartmeeting)
	 */
	@Override
	public void addPartmeeting(SysPartmeeting partmeeting) {
		
		partmeeting.setId(UUIDUtil.getUUID());
		partmeeting.setDeleteStatus(0);
		partmeeting.setVersion(0);
		partmeetingMapper.insert(partmeeting);
	}

	/** 
	 * (non Javadoc) 
	 * @Title: listPartmeetings
	 * @param params
	 * @return
	 * @see com.v4ward.meeting.service.PartmeetingSrv#listPartmeetings(java.util.Map)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<Map<String, Object>> listPartmeetings(Map params) {

		return partmeetingMapper.listPartmeetings(params);
	}

	/** 
	 * (non Javadoc) 
	 * @Title: editPartmeeting
	 * @param partmeeting
	 * @see com.v4ward.meeting.service.PartmeetingSrv#editPartmeeting(com.v4ward.meeting.model.SysPartmeeting)
	 */
	@Override
	public void editPartmeeting(SysPartmeeting partmeeting) {
		partmeetingMapper.edit(partmeeting);
	}

	/** 
	 * (non Javadoc) 
	 * @Title: deletePartmeting
	 * @param id
	 * @see com.v4ward.meeting.service.PartmeetingSrv#deletePartmeting(java.lang.String)
	 */
	@Override
	public void deletePartmeting(String id) {
		partmeetingMapper.deleteById(id);
	}

	/** 
	 * 根据分会场状态修改会议状态
	 * @Title: checkMeetingStatusComplete
	 * @param meetingId
	 * @see com.v4ward.meeting.service.PartmeetingSrv#checkMeetingStatusComplete(java.lang.String)
	 */
	@Override
	public void checkMeetingStatusComplete(String meetingId) {
		partmeetingMapper.checkMeetingStatusComplete(meetingId);
	}
	
	
	/** 
	 * (non Javadoc) 
	 * @Title: insertResources
	 * @param resource
	 * @see com.v4ward.meeting.service.PartmeetingSrv#insertResources(com.v4ward.meeting.model.SysResources)
	 */
	@Override
	public void insertResources(SysResources resource) {
		resourcesMapper.insert(resource);
	}

	/** 
	 * (non Javadoc) 
	 * @Title: deleteResourcesById
	 * @param id
	 * @see com.v4ward.meeting.service.PartmeetingSrv#deleteResourcesById(java.lang.String)
	 */
	@Override
	public void deleteResourcesById(String id) {
		resourcesMapper.deleteById(id);
	}

	/** 
	 * (non Javadoc) 
	 * @Title: listResourcesByPartmeetingId
	 * @param partmeetingId
	 * @return
	 * @see com.v4ward.meeting.service.PartmeetingSrv#listResourcesByPartmeetingId(java.lang.String)
	 */
	@Override
	public List<Map<String, Object>> listResourcesByPartmeetingId(String partmeetingId) {
		
		return resourcesMapper.listResourcesByPartmeetingId(partmeetingId);
	}

	/** 
	 * (non Javadoc) 
	 * @Title: getResource
	 * @param id
	 * @return
	 * @see com.v4ward.meeting.service.PartmeetingSrv#getResource(java.lang.String)
	 */
	@Override
	public SysResources getResource(String id) {
		
		return resourcesMapper.getResource(id);
	}

	/** 
	 * (non Javadoc) 
	 * @Title: editResource
	 * @param resources
	 * @see com.v4ward.meeting.service.PartmeetingSrv#editResource(com.v4ward.meeting.model.SysResources)
	 */
	@Override
	public void editResource(SysResources resources) {
		resourcesMapper.editResource(resources);
	}
	
	
	/** 
	 * (non Javadoc) 
	 * @Title: importExcel
	 * @param partmeetingUserList
	 * @see com.v4ward.meeting.service.PartmeetingSrv#importExcel(java.util.List)
	 */
	@Override
    public void importExcel(List<SysPartmeetingUser> partmeetingUserList) {
		
		partmeetingUserMapper.insertList(partmeetingUserList);
    }

	/** 
	 * (non Javadoc) 
	 * @Title: selectUserIdByPhone
	 * @param phone
	 * @return
	 * @see com.v4ward.meeting.service.PartmeetingSrv#selectUserIdByPhone(java.lang.String)
	 */
	@Override
	public String selectUserIdByPhone(String phone) {
		
		return partmeetingUserMapper.selectUserIdByPhone(phone);
	}

	/** 
	 * (non Javadoc) 
	 * @Title: getPartmeetingUserIdByPartmeetingIdAndUserId
	 * @param partmeetingId
	 * @param userId
	 * @return
	 * @see com.v4ward.meeting.service.PartmeetingSrv#getPartmeetingUserIdByPartmeetingIdAndUserId(java.lang.String, java.lang.String)
	 */
	@Override
	public String getPartmeetingUserIdByPartmeetingIdAndUserId(String partmeetingId, String userId) {
		
		return partmeetingUserMapper.getPartmeetingUserIdByPartmeetingIdAndUserId(partmeetingId, userId);
	}

	/** 
	 * (non Javadoc) 
	 * @Title: userListInsert
	 * @param userListInsert
	 * @see com.v4ward.meeting.service.PartmeetingSrv#userListInsert(java.util.List)
	 */
	@Override
	public void userListInsert(List<SysPartmeetingUser> userListInsert) {
		partmeetingUserMapper.userListInsert(userListInsert);
	}

	/** 
	 * (non Javadoc) 
	 * @Title: userListUpdate
	 * @param userListUpdate
	 * @see com.v4ward.meeting.service.PartmeetingSrv#userListUpdate(java.util.List)
	 */
	@Override
	public void userListUpdate(List<SysPartmeetingUser> userListUpdate) {
		partmeetingUserMapper.userListUpdate(userListUpdate);
	}

	@Override
	public void delByPartmeetingIdAndUserId(String partmeetingId, String str) {
		partmeetingUserMapper.delByPartmeetingIdAndUserId(partmeetingId,str);
	}


}
