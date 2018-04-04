/**   
 * Copyright © 2018 武汉现代物华科技发展有限公司信息分公司. All rights reserved.
 * 
 * @Title: SubmeetingController.java 
 * @Prject: wisdri-meeting
 * @Package: com.v4ward.meeting.controller
 * @author: 彭浩
 * @date: 2018年3月5日 下午3:29:01 
 * @version: V1.0   
 */
package com.v4ward.meeting.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
import com.v4ward.meeting.model.SysSubmeeting;
import com.v4ward.meeting.service.SubmeetingSrv;

/** 
 * 议程管理controller
 * @ClassName: SubmeetingController
 * @author: 彭浩
 * @date: 2018年3月5日 下午3:29:01  
 */
@Controller
@RequestMapping("/submeeting")
public class SubmeetingController extends BaseController {
	
	private static String PREFIX = "/v4ward/submeeting/";

    @Resource
    SubmeetingSrv submeetingSrv;

    /** 
     * 跳转议程管理页面
     * @Title: tosubmeeting
     * @return
     * @return: String
     */
    @RequestMapping("")
    public String tosubmeeting(@RequestParam("partmeetingId") String partmeetingId,
    		@RequestParam("partmeetingTitle") String partmeetingTitle,Model model) {
    	
    	model.addAttribute("partmeetingId",partmeetingId);
    	model.addAttribute("partmeetingTitle",partmeetingTitle);
    	return PREFIX + "submeeting";
    }
    
    /**
     * 跳转到新增议程页面
     */
    @RequestMapping(value = "/toadd")
    public String toadd(@RequestParam String partmeetingId,Model model) {
    	model.addAttribute("partmeetingId", partmeetingId);
        return PREFIX + "submeeting_add";
    }
    
    /**
     * 查看页面
     */
	@RequestMapping(value = "/toview/{id}")
	public String toview(@PathVariable String id, Model model) {

		if (ToolUtil.isEmpty(id)) {
			throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
		}
		SysSubmeeting submeeting = this.submeetingSrv.selectSubmeetingById(id);

		model.addAttribute("submeeting", submeeting);
		return PREFIX + "submeeting_view";
	}
    
    /**
    * 修改页面
    */
    @RequestMapping(value = "/tomodify/{id}")
    public String tomodify(@PathVariable String id, Model model) {
    	if (ToolUtil.isEmpty(id)) {
			throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
		}
		SysSubmeeting submeeting = this.submeetingSrv.selectSubmeetingById(id);

		model.addAttribute("submeeting", submeeting);
    	return PREFIX + "submeeting_modify";
    }

    /** 
     * 获取议程列表
     * @Title: list
     * @param partmeetingId
     * @param submeetingTheme
     * @param reuqest
     * @return
     * @return: Object
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam("partmeetingId") String partmeetingId,
    		@RequestParam("submeetingTheme") String submeetingTheme,HttpServletRequest reuqest) {
    	
    	Map<String,Object> para = new HashMap<String,Object>();
    	para.put("partmeetingId", partmeetingId);
    	para.put("submeetingTheme", submeetingTheme);
    	
    	new PageFactory<Map<String, Object>>().defaultPage();
    	List<Map<String, Object>> list = submeetingSrv.listSubmeetings(para);
    	
        PageInfo pageInfo = new PageInfo(list);
        Page pageList = (Page) list;
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("total",pageInfo.getTotal());
        result.put("rows", pageList);

        return result;
    }


    /**
     * 新增议程
     */
    @RequestMapping(value = "/submeetingAdd")
    @ResponseBody
    public Tip add(@RequestBody SysSubmeeting submeeting, BindingResult result) {
        if (result.hasErrors()) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        submeetingSrv.addSubmeeting(setCreateEmpAndTime(submeeting));
        return SUCCESS_TIP;
    }



    /**
     * 删除议程
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Tip delete(@RequestParam String submeetingId) {
        if (ToolUtil.isEmpty(submeetingId)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }

        submeetingSrv.deleteSubmeting(submeetingId);;
        return SUCCESS_TIP;
    }

    
    
    /**
     * 修改议程
     */
    @RequestMapping(value = "/submeetingEdit")
    @ResponseBody
    public Tip edit(@RequestBody SysSubmeeting submeeting, BindingResult result) {
        if (result.hasErrors()) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }

        this.submeetingSrv.editSubmeeting(setUpdateEmpAndTime(submeeting));
        return SUCCESS_TIP;
    }
	

}
