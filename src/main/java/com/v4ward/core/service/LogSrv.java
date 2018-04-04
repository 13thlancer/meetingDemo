package com.v4ward.core.service;

import com.v4ward.core.model.OperationLog;

import java.util.List;
import java.util.Map;

public interface LogSrv {
    List<Map<String,Object>> getOperationLogs(String beginTime, String endTime, String logName, String s);

    OperationLog selectById(String id);

    void delete();
}
