package com.v4ward.core.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.v4ward.core.annotation.BussinessLog;
import com.v4ward.core.kit.BeanKit;
import com.v4ward.core.warpper.LogWarpper;
import com.v4ward.core.annotation.Permission;
import com.v4ward.core.common.BizLogType;
import com.v4ward.core.model.OperationLog;
import com.v4ward.core.service.LogSrv;
import com.v4ward.core.factory.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @Description: 日志管理的控制器
 *
 * @ClassName: LogController
 *
 * @author huangxiang
 * @date 2017/12/26 14:28
 */
@Controller
@RequestMapping("/log")
public class LogController extends BaseController{

    private static String PREFIX = "/system/log/";

    @Autowired
    private LogSrv logSrv;


    /**
     * 跳转到日志管理的首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "log";
    }

    /**
     * 查询操作日志列表
     */
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String beginTime, @RequestParam(required = false) String endTime, @RequestParam(required = false) String logName, @RequestParam(required = false) Integer logType) {
        new PageFactory<OperationLog>().defaultPage();
        List<Map<String, Object>> list = logSrv.getOperationLogs(beginTime, endTime, logName, BizLogType.valueOf(logType));
        PageInfo pageInfo = new PageInfo( (List<OperationLog>) new LogWarpper(list).warp());
        Page pageList = (Page) list;
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("total",pageInfo.getTotal());
        result.put("rows", pageList);
        return result;
    }

    /**
     * 查询操作日志详情
     */
    @RequestMapping("/detail/{id}")
    @ResponseBody
    public Object detail(@PathVariable String id) {
        OperationLog operationLog = logSrv.selectById(id);
        Map<String, Object> stringObjectMap = BeanKit.beanToMap(operationLog);
        return super.warpObject(new LogWarpper(stringObjectMap));
    }

    /**
     * 清空日志
     */
    @RequestMapping("/delLog")
    @ResponseBody
    public Object delLog() {
        logSrv.delete();
        return super.SUCCESS_TIP;
    }



}
