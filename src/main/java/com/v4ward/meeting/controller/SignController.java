/**   
 * Copyright © 2018 武汉现代物华科技发展有限公司信息分公司. All rights reserved.
 * 
 * @Title: SignController.java 
 * @Prject: wisdri-meeting
 * @Package: com.v4ward.meeting.controller
 * @author: 彭浩
 * @date: 2018年3月15日 下午3:29:01 
 * @version: V1.0   
 */
package com.v4ward.meeting.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.v4ward.core.controller.BaseController;
import com.v4ward.core.exception.BizExceptionEnum;
import com.v4ward.core.exception.BussinessException;
import com.v4ward.core.factory.PageFactory;
import com.v4ward.core.tips.Tip;
import com.v4ward.core.utils.ToolUtil;
import com.v4ward.core.utils.poi.PoiUtils;
import com.v4ward.meeting.model.SysPartmeetingUser;
import com.v4ward.meeting.poiEntity.PartmeetingUserPoiEntity;
import com.v4ward.meeting.service.SignSrv;

/** 
 * 签到统计controller
 * @ClassName: SignController
 * @author: 彭浩
 * @date: 2018年3月14日 上午9:42:22  
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Controller
@RequestMapping("/sign")
public class SignController extends BaseController {
	
	private static String PREFIX = "/v4ward/sign/";

    @Resource
    SignSrv signSrv;

    /** 
     * 跳转签到信息主页
     * @Title: tosign
     * @param partmeetingId
     * @param meetingTheme
     * @param partmeetingTitle
     * @param model
     * @return
     * @return: String
     */
    @RequestMapping("")
    public String tosign(@RequestParam("partmeetingId") String partmeetingId,
    		@RequestParam("meetingTheme") String meetingTheme,
    		@RequestParam("partmeetingTitle") String partmeetingTitle,Model model) {
    	
    	model.addAttribute("partmeetingId", partmeetingId);
    	model.addAttribute("meetingTheme", meetingTheme);
    	model.addAttribute("partmeetingTitle", partmeetingTitle);
    	return PREFIX + "sign";
    }



    /**
     * 跳转分会场参会人员修改页面
     * @Title: topartmeeting
     * @param id
     * @param model
     * @return
     * @return: String
     */
    @RequestMapping("/toUpdateMeetingUser/{id}")
    public String toUpdateMeetingUser(@PathVariable String id, Model model) {
        SysPartmeetingUser PUser = this.signSrv.getPartMeetingUserById(id);
        model.addAttribute("PUser", PUser);
        return PREFIX + "sign_update";
    }



    /** 
     * 签到信息统计列表及查询
     * @Title: list
     * @param request
     * @param response
     * @return
     * @return: Object
     */
	@RequestMapping(value = "/list")
    @ResponseBody
    public Object list(HttpServletRequest request,HttpServletResponse response) {
    	
    	Map<String,Object> param = new HashMap<String,Object>();
    	param.put("partmeetingId", request.getParameter("partmeetingId"));
    	param.put("account", request.getParameter("account"));
    	param.put("name", request.getParameter("name"));
    	param.put("signStatus", request.getParameter("signStatus"));
    	
    	new PageFactory<PartmeetingUserPoiEntity>().defaultPage();
    	List<PartmeetingUserPoiEntity> list = signSrv.listSignByPartmeetingId(param);
    	
        PageInfo pageInfo = new PageInfo(list);
        Page pageList = (Page) list;
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("total",pageInfo.getTotal());
        result.put("rows", pageList);

        return result;
    }


    /**
     * 请假
     */
    @RequestMapping(value = "/leave")
    @ResponseBody
    public Tip leave(@RequestParam String id) {
        if (ToolUtil.isEmpty(id)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }

        signSrv.leave(id);
        return SUCCESS_TIP;
    }

    /**
     * 修改参会人员
     */
    @RequestMapping(value = "/updateMeetingUser")
    @ResponseBody
    public Tip updateMeetingUser(SysPartmeetingUser pUser){
        this.signSrv.updateMeetingUser(pUser);
        return SUCCESS_TIP;
    }

    /**
     * 导出所有数据excel
     */
    @RequestMapping("/export")
    @ResponseBody
    public void export(HttpServletRequest request,HttpServletResponse response){
    	
    	Map<String,Object> param = new HashMap<String,Object>();
    	param.put("partmeetingId", request.getParameter("partmeetingId"));
    	String partmeetingTitle = request.getParameter("partmeetingTitle");
    	List<PartmeetingUserPoiEntity> list = signSrv.listSignByPartmeetingId(param);
    	
        //模拟从数据库获取需要导出的数据
        //导出
		PoiUtils.exportExcel(list, "《" + partmeetingTitle + "》人员签到统计表", "签到统计", PartmeetingUserPoiEntity.class,
				"《" + partmeetingTitle + "》人员签到统计表.xls",response);
    }
    
    
    /** 
     * 跳转到参会统计页面
     * @Title: toattendCount
     * @return
     * @return: String
     */
    @RequestMapping("/attendCount")
    public String toattendCount() {
        return PREFIX + "attendCount";
    }
    

    /**
     * 跳转到参会记录页面
     */
    @RequestMapping("/toAttendRecord/{userId}")
    public String toAttendRecord(@PathVariable String userId,Model model) {
    	model.addAttribute("userId",userId);
        return PREFIX + "attendRecord";
    }
    
    
    
	/** 
	 * 根据userId查询 所有分会场列表
	 * @Title: listAttendRecord
	 * @param userId
	 * @return
	 * @return: Object
	 */
	@RequestMapping(value = "/listAttendRecord")
    @ResponseBody
    public Object listAttendRecord(@RequestParam String userId) {
    	
    	new PageFactory<PartmeetingUserPoiEntity>().defaultPage();
    	List<Map<String,Object>> list = signSrv.listAttendRecordByUserId(userId);
    	
        PageInfo pageInfo = new PageInfo(list);
        Page pageList = (Page) list;
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("total",pageInfo.getTotal());
        result.put("rows", pageList);

        return result;
    }

    /**
     * 删除参会人员
     */
    @RequestMapping(value = "/delMeetingUser")
    @ResponseBody
    public Tip delMeetingUser(String id){
        this.signSrv.delMeetingUser(id);
        return SUCCESS_TIP;
    }
}
