package com.v4ward.meeting.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.v4ward.meeting.model.SysMeeting;
import com.v4ward.meeting.model.SysMeetingUser;

@SuppressWarnings("rawtypes")
@Repository
public interface MeetingMapper {

	SysMeeting selectMeetingById(String id);

    void insert(SysMeeting meeting);

	List<Map<String, Object>> listMeetings(Map params);
	/** 
	 * 根据RoleType过滤会议条件
	 * @Title: listMeetingsByRoleType
	 * @param params
	 * @return
	 * @return: List<Map<String,Object>>
	 */
	List<Map<String, Object>> listMeetingsByRoleType(Map params);

    void deleteById(String id);

    void edit(SysMeeting meeting);
    
    /** 
     * 获取超级用户角色对应的OrgId
     * @Title: getSuperRoleOrgId
     * @param managerId
     * @return
     * @return: String
     */
    String getSuperRoleOrgId(String managerId);
    
    
    /** 
     * 获取会务管理员列表
     * @Title: getMeetingManager
     * @param meetingId
     * @return
     * @return: List<Map<String,Object>>
     */
    List<Map<String, Object>> getMeetingManager(String meetingId);
    
    
    /** 
     * 根据会议id删除 会议管理员
     * @Title: deleteMeetingUserByMeetingId
     * @param meetingId
     * @return: void
     */
    void deleteMeetingUserByMeetingId(String meetingId);
    
    
    /** 
     * 批量新增会议管理员
     * @Title: insertMeetingUserList
     * @param list
     * @return: void
     */
    void insertMeetingUserList(List<SysMeetingUser> meetingUsers);
}
