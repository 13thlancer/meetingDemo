package com.v4ward.meeting.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.v4ward.meeting.model.SysPartmeeting;
import com.v4ward.meeting.model.SysPartmeetingUser;
import com.v4ward.meeting.model.SysResources;

public interface PartmeetingSrv {
	
	
    /** 
     * 根据主键查询对象
     * @Title: selectPartmeetingById
     * @param id
     * @return
     * @return: SysMeeting
     */
    SysPartmeeting selectPartmeetingById(String id);

    /** 
     * 新增会议
     * @Title: addPartmeeting
     * @param meeting
     * @return: void
     */
    void addPartmeeting(SysPartmeeting partmeeting);

    /** 
     * 查询会议列表
     * @Title: listPartmeetings
     * @param params
     * @return
     * @return: List<Map<String,Object>>
     */
    @SuppressWarnings("rawtypes")
	List<Map<String, Object>> listPartmeetings(Map params);

    /** 
     * 修改会议信息
     * @Title: editPartmeeting
     * @param meeting
     * @return: void
     */
    void editPartmeeting(SysPartmeeting partmeeting);

    /** 
     * 删除会议信息
     * @Title: deletePartmeting
     * @param id
     * @return: void
     */
    void deletePartmeting(String id);
    
    
    /** 
     * 根据分会场状态修改会议状态,分会场全部“完成”后，会议自动“完成”
     * @Title: checkMeetingStatusComplete
     * @param meetingId
     * @return: void
     */
    void checkMeetingStatusComplete(String meetingId); 
    
    
	/** 
	 * 根据partmeetingId获取已传文件列表
	 * @Title: listResourcesByPartmeetingId
	 * @param partmeetingId
	 * @return
	 * @return: List<Map<String,Object>>
	 */
	List<Map<String,Object>> listResourcesByPartmeetingId(String partmeetingId);

    /** 
     * 新增会议材料数据
     * @Title: insertResources
     * @param resource
     * @return: void
     */
    void insertResources(SysResources resource);

    /** 
     * 删除会议材料by Id
     * @Title: deleteResourcesById
     * @param id
     * @return: void
     */
    void deleteResourcesById(String id);
    
    SysResources getResource(String id);
    
    void editResource(SysResources resources);
    
    /** 
     * 导入 ,如果是重复数据就不导入
     * @Title: importExcel
     * @param orgList
     * @return
     * @return: Tip
     */
    void importExcel(List<SysPartmeetingUser> partmeetingUserList);
    
    String selectUserIdByPhone(String phone);
    
    
    /** 
     * 
     * @Title: getPartmeetingUserIdByPartmeetingIdAndUserId
     * @param partmeetingId
     * @param userId
     * @return
     * @return: String
     */
    String getPartmeetingUserIdByPartmeetingIdAndUserId(
    		@Param("partmeetingId")String partmeetingId,@Param("userId")String userId);
    

    
    /** 
     * 导入 ,全部新增
     * @Title: userListInsert
     * @param userListInsert
     * @return: void
     */
    void userListInsert(List<SysPartmeetingUser> userListInsert);
    
    
    
    /** 
     * 导入 ,全部修改
     * @Title: userListUpdate
     * @param userListUpdate
     * @return: void
     */
    void userListUpdate(List<SysPartmeetingUser> userListUpdate);

    /**
     *
     * @Description: 根据会议Id和人员Id删除参会人员
     *
     *
     * @MethodName: delByPartmeetingIdAndUserId
     * @Parameters: [meetingId, str]
     * @ReturnType: void
     *
     * @author huangxiang
     * @date 2018/3/16 17:16
     */
    void delByPartmeetingIdAndUserId(String partmeetingId, String str);
}
