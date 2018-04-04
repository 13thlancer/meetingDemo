package com.v4ward.meeting.service;

import java.util.List;
import java.util.Map;

import com.v4ward.meeting.model.SysMeeting;

public interface MeetingSrv {
	
	
    /** 
     * 根据主键查询对象
     * @Title: selectMeetingById
     * @param id
     * @return
     * @return: SysMeeting
     */
    SysMeeting selectMeetingById(String id);

    /** 
     * 新增会议
     * @Title: addMeeting
     * @param meeting
     * @return: void
     */
    void addMeeting(SysMeeting meeting);

    /** 
     * 查询会议列表
     * @Title: list
     * @param params
     * @return
     * @return: List<Map<String,Object>>
     */
    @SuppressWarnings("rawtypes")
	List<Map<String, Object>> listMeetings(Map params);
    
    /** 
     * 根据RoleType过滤会议条件
     * @Title: list
     * @param params
     * @return
     * @return: List<Map<String,Object>>
     */
    @SuppressWarnings("rawtypes")
    List<Map<String, Object>> listMeetingsByRoleType(Map params);

    /** 
     * 修改会议信息
     * @Title: editMeeting
     * @param meeting
     * @return: void
     */
    void editMeeting(SysMeeting meeting);

    /** 
     * 删除会议信息
     * @Title: deleteMeeting
     * @param id
     * @return: void
     */
    void deleteMeeting(String id);
    
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
     * 分配新的会务管理员 ，先清空，再新增
     * @Title: allotManager
     * @param meetingId
     * @param managerIdList
     * @return: void
     */
    void allotManager(String meetingId,List<String> managerIdList);

}
