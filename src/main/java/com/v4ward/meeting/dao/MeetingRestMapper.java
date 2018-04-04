package com.v4ward.meeting.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.v4ward.meeting.model.SysMeeting;
import com.v4ward.meeting.model.SysPartmeetingUser;
import com.v4ward.meeting.model.SysResources;
import com.v4ward.meeting.model.SysSubmeeting;

@Repository
public interface MeetingRestMapper {

	public List<SysSubmeeting> selectSysSubmeetingByPartmeetingId(@Param("partmeetingId") String partmeetingId);

	public List<SysResources> selectSysResourcesByPartmeetingId(@Param("partmeetingId") String partmeetingId);

	public SysMeeting selectMeetingByPartmeetingId(@Param("partmeetingId") String partmeetingId);

	public List<Map<String, Object>> selectCurrentPartmeeting(Map<String, Object> param);

	public List<Map<String, Object>> selectHistoryPartmeeting(Map<String, Object> param);

	public List<SysPartmeetingUser> selectSysPartmeetingUser(Map<String, Object> param);

}
