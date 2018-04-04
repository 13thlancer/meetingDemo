package com.v4ward.meeting.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.v4ward.meeting.model.SysPartmeetingUser;

@Repository
public interface SignRestMapper {

	public void insertSysPartmeetingUser(SysPartmeetingUser sysPartmeetingUser);

	public int updateSysPartmeetingUser(SysPartmeetingUser sysPartmeetingUser);

	public SysPartmeetingUser selectSysPartmeetingUserById(@Param("partmeetingId") String partmeetingId,
			@Param("userId") String userId);

	public List<Map<String, Object>> selectSysPartmeetingUser(Map<String, Object> param);

}
