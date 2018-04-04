/**   
 * Copyright © 2018 武汉现代物华科技发展有限公司信息分公司. All rights reserved.
 * 
 * @Title: SignSrvImpl.java 
 * @Prject: wisdri-meeting
 * @Package: com.v4ward.meeting.service.impl
 * @author: 彭浩
 * @date: 2018年3月14日 上午9:53:23 
 * @version: V1.0   
 */
package com.v4ward.meeting.service.impl;

import java.util.List;
import java.util.Map;

import com.v4ward.meeting.model.SysPartmeetingUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.v4ward.meeting.dao.PartmeetingUserMapper;
import com.v4ward.meeting.poiEntity.PartmeetingUserPoiEntity;
import com.v4ward.meeting.service.SignSrv;

/** 
 * 签到统计service
 * @ClassName: SignSrvImpl
 * @author: 彭浩
 * @date: 2018年3月14日 上午9:53:23  
 */
@Service
@Transactional
public class SignSrvImpl implements SignSrv {
	
	@Autowired
	PartmeetingUserMapper partmeetingUserMapper;

	/** 
	 * (non Javadoc) 
	 * @Title: listSignByPartmeetingId
	 * @param params
	 * @return
	 * @see com.v4ward.meeting.service.SignSrv#listSignByPartmeetingId(java.util.Map)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<PartmeetingUserPoiEntity> listSignByPartmeetingId(Map params) {

		return partmeetingUserMapper.listSignByPartmeetingId(params);
	}

	/** 
	 * (non Javadoc) 
	 * @Title: leave
	 * @param id
	 * @see com.v4ward.meeting.service.SignSrv#leave(java.lang.String)
	 */
	@Override
	public void leave(String id) {
		partmeetingUserMapper.leave(id);
	}

	/** 
	 * (non Javadoc) 
	 * @Title: listAttendRecordByUserId
	 * @param userId
	 * @return
	 * @see com.v4ward.meeting.service.SignSrv#listAttendRecordByUserId(java.lang.String)
	 */
	@Override
	public List<Map<String, Object>> listAttendRecordByUserId(String userId) {
		
		return partmeetingUserMapper.listAttendRecordByUserId(userId);
	}

	/**
	 *
	 * @Description: 根据id返回参会人员信息
	 *
	 *
	 * @MethodName: getPartMeetingUserById
	 * @Parameters: [id] 
	 * @ReturnType: com.v4ward.meeting.model.SysPartmeetingUser
	 *
	 * @date 2018/3/16 11:11
	 */
	@Override
	public SysPartmeetingUser getPartMeetingUserById(String id) {
		return partmeetingUserMapper.getPartMeetingUserById(id);
	}

	/**
	 *
	 * @Description: 根据id修改参会人员信息
	 *
	 *
	 * @MethodName: updateMeetingUser
	 * @Parameters: [pUser]
	 * @ReturnType: void
	 *
	 * @date 2018/3/16 11:11
	 */
	@Override
	public void updateMeetingUser(SysPartmeetingUser pUser) {
		this.partmeetingUserMapper.updateMeetingUser(pUser);
	}

	@Override
	public void delMeetingUser(String id) {
		this.partmeetingUserMapper.delMeetingUser(id);
	}

}
