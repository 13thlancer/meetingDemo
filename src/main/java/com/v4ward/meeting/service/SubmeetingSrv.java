package com.v4ward.meeting.service;

import java.util.List;
import java.util.Map;

import com.v4ward.meeting.model.SysSubmeeting;

public interface SubmeetingSrv {
	
	
    /** 
     * 根据主键查询对象
     * @Title: selectSubmeetingById
     * @param id
     * @return
     * @return: SysSubmeeting
     */
    SysSubmeeting selectSubmeetingById(String id);

    /** 
     * 新增议程
     * @Title: addSubmeeting
     * @param meeting
     * @return: void
     */
    void addSubmeeting(SysSubmeeting submeeting);

    /** 
     * 查询议程列表
     * @Title: listSubmeetings
     * @param params
     * @return
     * @return: List<Map<String,Object>>
     */
    @SuppressWarnings("rawtypes")
	List<Map<String, Object>> listSubmeetings(Map params);

    /** 
     * 修改议程信息
     * @Title: editSubmeeting
     * @param meeting
     * @return: void
     */
    void editSubmeeting(SysSubmeeting submeeting);

    /** 
     * 删除议程信息
     * @Title: deleteSubmeting
     * @param id
     * @return: void
     */
    void deleteSubmeting(String id);
    
}
