package com.v4ward.meeting.service;

import java.util.Map;

public interface SignRestSrv {

	public Map<String, Object> signScanOrdinary(String partmeetingId, String userId) throws Exception;

	public Map<String, Object> signScanSpecial(String partmeetingId, String attendanceId, String recorderId)
			throws Exception;

	public Map<String, Object> checksignin(String partmeetingId, String userId) throws Exception;

	public Map<String, Object> statisticsTotal(String partmeetingId) throws Exception;

	public Map<String, Object> statisticsDetails(String partmeetingId, String status) throws Exception;

}
