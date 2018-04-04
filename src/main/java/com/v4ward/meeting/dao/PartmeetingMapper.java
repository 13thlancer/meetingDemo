package com.v4ward.meeting.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.v4ward.meeting.model.SysPartmeeting;

@Repository
public interface PartmeetingMapper {

	SysPartmeeting selectPartmeetingById(String id);

    void insert(SysPartmeeting partmeeting);

    @SuppressWarnings("rawtypes")
	List<Map<String, Object>> listPartmeetings(Map params);

    void deleteById(String id);

    void edit(SysPartmeeting partmeeting);
    
    /** 
     * 根据分会场状态修改会议状态
     * @Title: checkMeetingStatusComplete
     * @param meetingId
     * @return: void
     */
    void checkMeetingStatusComplete(String meetingId);
    
}
