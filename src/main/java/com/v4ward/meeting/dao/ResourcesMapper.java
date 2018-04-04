package com.v4ward.meeting.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.v4ward.meeting.model.SysResources;

@Repository
public interface ResourcesMapper {

	/** 
	 * 根据partmeetingId获取已传文件列表
	 * @Title: listResourcesByPartmeetingId
	 * @param partmeetingId
	 * @return
	 * @return: List<Map<String,Object>>
	 */
	List<Map<String,Object>> listResourcesByPartmeetingId(String partmeetingId);

    void insert(SysResources resource);

    void deleteById(String id);
    
    SysResources getResource(String id);
    
    void editResource(SysResources resources);
    
    
}
