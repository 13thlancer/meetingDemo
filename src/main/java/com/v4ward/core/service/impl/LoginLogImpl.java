package com.v4ward.core.service.impl;

import com.v4ward.core.service.LoginLogSrv;
import com.v4ward.core.dao.LoginLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class LoginLogImpl implements LoginLogSrv {

    @Autowired
    private LoginLogMapper loginLogMapper;


    @Override
    public void delete() {
        loginLogMapper.delete();
    }

    @Override
    public List<Map<String, Object>> getLoginLogs(String beginTime, String endTime, String logName) {
        return loginLogMapper.getLoginLogs(beginTime,endTime,logName);
    }
}
