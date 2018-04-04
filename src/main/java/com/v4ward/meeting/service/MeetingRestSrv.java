package com.v4ward.meeting.service;

import java.util.List;
import java.util.Map;

public interface MeetingRestSrv {

	public Map<String, Object> agenda(String partmeetingId) throws Exception;

	public Map<String, Object> notice(String partmeetingId) throws Exception;

	public Map<String, Object> material(String partmeetingId) throws Exception;

	public Map<String, Object> attendanceInfo(String partmeetingId, int lineNo) throws Exception;

	public Map<String, Object> selectCurrent(String userId, int lineNo) throws Exception;

	public Map<String, Object> selectHistory(String userId, int lineNo) throws Exception;

	@SuppressWarnings("rawtypes")
	public Map<String, Object> selectUserInfo(List<Map> idsArr) throws Exception;

}
