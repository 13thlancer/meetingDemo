package com.v4ward.core.service;

import java.util.List;
import java.util.Map;

public interface LoginLogSrv {

    void delete();

    List<Map<String,Object>> getLoginLogs(String beginTime, String endTime, String logName);
}
