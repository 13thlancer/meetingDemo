package com.v4ward.meeting.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.v4ward.meeting.model.SysPartmeetingUser;
import com.v4ward.meeting.poiEntity.PartmeetingUserPoiEntity;

@Repository
public interface PartmeetingUserMapper {

    /** 
     * 导入 ,如果是重复数据就不导入
     * @Title: insertList
     * @param partmeetingUserList
     * @return: void
     */
    void insertList(List<SysPartmeetingUser> partmeetingUserList);
    
    /** 
     * 根据手机号查询userId
     * @Title: selectUserIdByPhone
     * @param phone
     * @return
     * @return: String
     */
    String selectUserIdByPhone(String phone);
    
    /** 
     * 查询签到信息
     * 
     * @Title: listSignByPartmeetingId
     * @param params
     * @return
     * @return: List<PartmeetingUserPoiEntity>
     */
    @SuppressWarnings("rawtypes")
	List<PartmeetingUserPoiEntity> listSignByPartmeetingId(Map params) ;
    
    /** 
     * 请假
     * @Title: leave
     * @param id
     * @return: void
     */
    void leave(String id) ;
    
    
    /** 
     * 根据userId查询 所有分会场列表
     * @Title: listAttendRecordByUserId
     * @param userId
     * @return
     * @return: List<Map<String,Object>>
     */
    List<Map<String, Object>> listAttendRecordByUserId(String userId);
    
    
    /** 
     * 根据partmeetingId和userId查询SysPartmeetingUserId
     * @Title: getPartmeetingUserByPartmeetingIdAndUserId
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
    
    
    /***
     *
     * @Description: 根据id返回参会人员信息
     *
     *
     * @MethodName: getPartMeetingUserById 
     * @Parameters: [id] 
     * @ReturnType: com.v4ward.meeting.model.SysPartmeetingUser
     *
     */
    SysPartmeetingUser getPartMeetingUserById(@Param("id") String id);

    
    /**
     *
     * @Description: 根据id修改参会人员信息
     *
     *
     * @MethodName: updateMeetingUser
     * @Parameters: [pUser] 
     * @ReturnType: void
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
    void delMeetingUser(@Param("id") String id);

    /**
     *
     * @Description: 根据会议Id和人员Id删除参会人员
     *
     *
     * @MethodName: delByPartmeetingIdAndUserId
     * @Parameters: [partmeetingId, str] 
     * @ReturnType: void
     *
     * @author huangxiang
     * @date 2018/3/16 17:17
     */
    void delByPartmeetingIdAndUserId(@Param("partmeetingId")String partmeetingId,@Param("userId") String userId);
}
