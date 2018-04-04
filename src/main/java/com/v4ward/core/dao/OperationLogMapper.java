package com.v4ward.core.dao;


import com.v4ward.core.model.OperationLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 
 * @Description: 操作日志 Mapper 接口
 *
 * @ClassName: OperationLogMapper
 * 
 * @author huangxiang
 * @date 2017/12/26 10:28
 */

@Repository
public interface OperationLogMapper {

    void insert(OperationLog operationLog);

    List<Map<String, Object>> getOperationLogs(@Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("logName") String logName, @Param("logType") String logType);

    OperationLog selectById(String id);

    void delete();
}