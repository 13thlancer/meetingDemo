package com.v4ward.meeting.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.v4ward.meeting.model.SysSubmeeting;

@Repository
public interface SubmeetingMapper {

	SysSubmeeting selectSubmeetingById(String id);

    void insert(SysSubmeeting submeeting);

    @SuppressWarnings("rawtypes")
	List<Map<String, Object>> listSubmeetings(Map params);

    void deleteById(String id);

    void edit(SysSubmeeting partmeeting);
    
}
