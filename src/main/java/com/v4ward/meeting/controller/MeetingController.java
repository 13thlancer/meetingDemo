/**   
 * Copyright © 2018 武汉现代物华科技发展有限公司信息分公司. All rights reserved.
 * 
 * @Title: MeetingController.java 
 * @Prject: wisdri-meeting
 * @Package: com.v4ward.meeting.controller
 * @author: 彭浩
 * @date: 2018年3月5日 下午3:29:01 
 * @version: V1.0   
 */
package com.v4ward.meeting.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
import com.v4ward.meeting.model.SysMeeting;
import com.v4ward.meeting.service.MeetingSrv;

/** 
 * 会议管理controller
 * @ClassName: MeetingController
 * @author: 彭浩
 * @date: 2018年3月5日 下午3:29:01  
 */
@Controller
@RequestMapping("/meeting")
public class MeetingController extends BaseController {
	
	private static String PREFIX = "/v4ward/meeting/";

    @Resource
    MeetingSrv meetingSrv;

    /** 
     * 跳转会议管理主页
     * @Title: tomeeting
     * @return
     * @return: String
     */
    @RequestMapping("")
    public String tomeeting( Model model) {
    	
    	String roleType = Arrays.toString(getLoginUserRoleType());
    	model.addAttribute("roleType", roleType);
    	return PREFIX + "meeting";
    }
    
    /**
     * 跳转到新增会议页面
     */
    @RequestMapping(value = "/toadd")
    public String toadd() {
        return PREFIX + "meeting_add";
    }
    
    /**
     * 查看页面
     */
	@RequestMapping(value = "/toview/{id}")
	public String toview(@PathVariable String id, Model model) {

		if (ToolUtil.isEmpty(id)) {
			throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
		}
		SysMeeting meeting = this.meetingSrv.selectMeetingById(id);

		model.addAttribute("meeting", meeting);
		return PREFIX + "meeting_view";
	}
    
    /**
    * 修改页面
    */
    @RequestMapping(value = "/tomodify/{id}")
    public String tomodify(@PathVariable String id, Model model) {
    	if (ToolUtil.isEmpty(id)) {
			throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
		}
		SysMeeting meeting = this.meetingSrv.selectMeetingById(id);

		model.addAttribute("meeting", meeting);
    	return PREFIX + "meeting_modify";
    }

    /** 
     * 获取会议列表
     * @Title: list
     * @param reuqest
     * @param response
     * @return
     * @return: Object
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/list")
    @ResponseBody
    public Object list(HttpServletRequest reuqest,HttpServletResponse response) {
    	
    	Map<String,Object> param = new HashMap<String,Object>();
    	param.put("meetingTheme", reuqest.getParameter("meetingTheme"));
    	param.put("userId", getLoginUserId());//登录人Id
    	
    	new PageFactory<Map<String, Object>>().defaultPage();
//    	List<Map<String, Object>> list = meetingSrv.listMeetings(para);
    	List<Map<String, Object>> list = meetingSrv.listMeetingsByRoleType(param);
    	
        PageInfo pageInfo = new PageInfo(list);
        Page pageList = (Page) list;
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("total",pageInfo.getTotal());
        result.put("rows", pageList);
        
        return result;
    }


    /**
     * 新增会议
     */
    @RequestMapping(value = "/meetingAdd")
    @ResponseBody
    public Tip add(@RequestBody SysMeeting meeting, BindingResult result) {
        if (result.hasErrors()) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        meeting.setSuperOrgId(meetingSrv.getSuperRoleOrgId(getLoginUserId()));//用户ID（关联超级角色所属组织)
        meetingSrv.addMeeting(setCreateEmpAndTime(meeting));
        return SUCCESS_TIP;
    }



    /**
     * 删除会议
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Tip delete(@RequestParam String meetingId) {
        if (ToolUtil.isEmpty(meetingId)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }

        meetingSrv.deleteMeeting(meetingId);;
        return SUCCESS_TIP;
    }

    
    
    /**
     * 修改会议
     */
    @RequestMapping(value = "/meetingEdit")
    @ResponseBody
    public Tip edit(@RequestBody SysMeeting meeting, BindingResult result) {
        if (result.hasErrors()) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }

        this.meetingSrv.editMeeting(setUpdateEmpAndTime(meeting));
        return SUCCESS_TIP;
    }
    
    
    
    /**
     * 跳转到分配会务管理员
     */
    @RequestMapping(value = "/openAllotManager")
    public String openAllotManager(@RequestParam("meetingId") String meetingId,
    		@RequestParam("meetingTheme") String meetingTheme, Model model) {
    	
    	List<Map<String, Object>> list = meetingSrv.getMeetingManager(meetingId);

    	model.addAttribute("meetingId", meetingId);
    	model.addAttribute("meetingTheme", meetingTheme);
    	model.addAttribute("meetingManagers", list);
        return PREFIX + "allotManager";
    }
    
    
    /** 
     * 分配会务管理员
     * @Title: allotMeetingManager
     * @param meetingId
     * @param managerIds
     * @return
     * @return: Tip
     */
    @RequestMapping(value = "/allotMeetingManager")
    @ResponseBody
    public Tip allotMeetingManager(@RequestParam("meetingId") String meetingId,
    		@RequestParam("managerIds") String managerIds) {
        if (ToolUtil.isEmpty(meetingId)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        
		List<String> managerIdList = null;
		if (ToolUtil.isNotEmpty(managerIds)) {
			managerIdList = Arrays.asList(managerIds.split(","));
		}

        meetingSrv.allotManager(meetingId, managerIdList);
        return SUCCESS_TIP;
    }
    
	

}
