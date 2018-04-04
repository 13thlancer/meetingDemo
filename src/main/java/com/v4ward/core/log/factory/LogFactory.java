package com.v4ward.core.log.factory;


import com.v4ward.core.common.LogSucceed;
import com.v4ward.core.common.LogType;
import com.v4ward.core.model.LoginLog;
import com.v4ward.core.model.OperationLog;

import java.util.Date;

/**
 * 
 * @Description: 日志对象创建工厂
 *
 * @ClassName: LogFactory
 * 
 * @author huangxiang
 * @date 2017/12/26 10:01
 */
public class LogFactory {

    public static OperationLog createOperationLog(LogType logType, String userId, String bussinessName, String clazzName, String methodName, String msg, LogSucceed succeed){
        OperationLog operationLog = new OperationLog();
        operationLog.setLogtype(logType.getMessage());
        operationLog.setLogname(bussinessName);
        operationLog.setUserid(userId);
        operationLog.setClassname(clazzName);
        operationLog.setMethod(methodName);
        operationLog.setCreatetime(new Date());
        operationLog.setSucceed(succeed.getMessage());
        operationLog.setMessage(msg);
        return operationLog;
    }


    public static LoginLog createLoginLog(LogType logType, String userId, String msg, String ip) {
        LoginLog loginLog = new LoginLog();
        loginLog.setLogname(logType.getMessage());
        loginLog.setUserid(userId);
        loginLog.setCreatetime(new Date());
        loginLog.setSucceed(LogSucceed.SUCCESS.getMessage());
        loginLog.setIp(ip);
        loginLog.setMessage(msg);
        return loginLog;
    }
}
