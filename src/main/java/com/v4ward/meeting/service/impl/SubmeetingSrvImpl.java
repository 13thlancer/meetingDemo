/**   
 * Copyright © 2018 武汉现代物华科技发展有限公司信息分公司. All rights reserved.
 * 
 * @Title: SubmeetingSrvImpl.java 
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
import com.v4ward.meeting.dao.SubmeetingMapper;
import com.v4ward.meeting.model.SysSubmeeting;
import com.v4ward.meeting.service.SubmeetingSrv;

/** 
 * 议程管理serviceImpl
 * @ClassName: SubmeetingSrvImpl
 * @author: 彭浩
 * @date: 2018年3月9日 上午10:55:48  
 */
@Service
@Transactional
public class SubmeetingSrvImpl implements SubmeetingSrv {

	@Autowired
	SubmeetingMapper submeetingMapper;
	
	/** 
	 * (non Javadoc) 
	 * @Title: selectSubmeetingById
	 * @param id
	 * @return
	 * @see com.v4ward.meeting.service.SubmeetingSrv#selectSubmeetingById(java.lang.String)
	 */
	@Override
	public SysSubmeeting selectSubmeetingById(String id) {

		return submeetingMapper.selectSubmeetingById(id);
	}

	/** 
	 * (non Javadoc) 
	 * @Title: addSubmeeting
	 * @param submeeting
	 * @see com.v4ward.meeting.service.SubmeetingSrv#addSubmeeting(com.v4ward.meeting.model.SysSubmeeting)
	 */
	@Override
	public void addSubmeeting(SysSubmeeting submeeting) {
		
		submeeting.setId(UUIDUtil.getUUID());
		submeeting.setVersion(0);
		submeetingMapper.insert(submeeting);
	}

	/** 
	 * (non Javadoc) 
	 * @Title: listSubmeetings
	 * @param params
	 * @return
	 * @see com.v4ward.meeting.service.SubmeetingSrv#listSubmeetings(java.util.Map)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<Map<String, Object>> listSubmeetings(Map params) {

		return submeetingMapper.listSubmeetings(params);
	}

	/** 
	 * (non Javadoc) 
	 * @Title: editSubmeeting
	 * @param submeeting
	 * @see com.v4ward.meeting.service.SubmeetingSrv#editSubmeeting(com.v4ward.meeting.model.SysSubmeeting)
	 */
	@Override
	public void editSubmeeting(SysSubmeeting submeeting) {
		submeetingMapper.edit(submeeting);
	}

	/** 
	 * (non Javadoc) 
	 * @Title: deleteSubmeting
	 * @param id
	 * @see com.v4ward.meeting.service.SubmeetingSrv#deleteSubmeting(java.lang.String)
	 */
	@Override
	public void deleteSubmeting(String id) {
		submeetingMapper.deleteById(id);
	}


}
