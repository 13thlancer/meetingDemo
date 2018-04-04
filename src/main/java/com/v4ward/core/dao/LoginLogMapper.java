package com.v4ward.core.dao;


import com.v4ward.core.model.LoginLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


/**
 * 
 * @Description: 登录记录 Mapper 接口
 *
 * @ClassName: LoginLogMapper
 * 
 * @author huangxiang
 * @date 2017/12/26 10:29
 */
@Repository
public interface LoginLogMapper {

    void insert(LoginLog loginLog);

    void delete();

    List<Map<String,Object>> getLoginLogs(@Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("logName") String logName);
}