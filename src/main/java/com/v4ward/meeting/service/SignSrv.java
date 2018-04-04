package com.v4ward.meeting.service;

import java.util.List;
import java.util.Map;

import com.v4ward.meeting.model.SysPartmeetingUser;
import com.v4ward.meeting.poiEntity.PartmeetingUserPoiEntity;

public interface SignSrv {
	

    /** 
     * 查询签到信息
     * 
     * @Title: listSignByPartmeetingId
     * @param params
     * @return
     * @return: List<PartmeetingUserPoiEntity>
     */
    @SuppressWarnings("rawtypes")
	List<PartmeetingUserPoiEntity> listSignByPartmeetingId(Map params);

    /** 
     * 请假
     * @Title: leave
     * @param id
     * @return: void
     */
    void leave(String id);
    
    
    /** 
     * 根据userId查询 所有分会场列表
     * @Title: listAttendRecordByUserId
     * @param userId
     * @return
     * @return: List<Map<String,Object>>
     */
    List<Map<String,Object>> listAttendRecordByUserId(String userId);

    /**
     *
     * @Description:根据id返回参会人员信息
     *
     *
     * @MethodName: getPartMeetingUserById
     * @Parameters: [id]
     * @ReturnType: com.v4ward.meeting.model.SysPartmeetingUser
     *
     * @date 2018/3/16 11:09
     */
    SysPartmeetingUser getPartMeetingUserById(String id);

    /**
     *
     * @Description: 根据id修改参会人员信息
     *
     *
     * @MethodName: updateMeetingUser
     * @Parameters: [pUser]
     * @ReturnType: void
     *
     * @date 2018/3/16 13:32
     */
    void updateMeetingUser(SysPartmeetingUser pUser);

    /**
     *
     * @Description: 根据id删除参会人员信息
     *
     *
     * @MethodName: delMeetingUser
     * @Parameters: [id] 
     * @ReturnType: void
     */
    void delMeetingUser(String id);
}
