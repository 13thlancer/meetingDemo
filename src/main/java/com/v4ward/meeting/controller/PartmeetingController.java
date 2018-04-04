/**   
 * Copyright © 2018 武汉现代物华科技发展有限公司信息分公司. All rights reserved.
 * 
 * @Title: PartmeetingController.java 
 * @Prject: wisdri-partmeeting
 * @Package: com.v4ward.partmeeting.controller
 * @author: 彭浩
 * @date: 2018年3月5日 下午3:29:01 
 * @version: V1.0   
 */
package com.v4ward.meeting.controller;

import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.v4ward.core.common.CoreConstant;
import com.v4ward.core.common.FileConstant;
import com.v4ward.core.controller.BaseController;
import com.v4ward.core.exception.BizExceptionEnum;
import com.v4ward.core.exception.BussinessException;
import com.v4ward.core.factory.PageFactory;
import com.v4ward.core.tips.ErrorTip;
import com.v4ward.core.tips.Tip;
import com.v4ward.core.utils.ToolUtil;
import com.v4ward.core.utils.UUIDUtil;
import com.v4ward.core.utils.UploadUtil;
import com.v4ward.core.utils.poi.PoiUtils;
import com.v4ward.meeting.model.SysPartmeeting;
import com.v4ward.meeting.model.SysPartmeetingUser;
import com.v4ward.meeting.model.SysResources;
import com.v4ward.meeting.poiEntity.PartmeetingUserPoiEntity;
import com.v4ward.meeting.poiEntity.PartmeetingUserTemplate;
import com.v4ward.meeting.service.PartmeetingSrv;
import com.v4ward.meeting.service.SignSrv;

import cn.hutool.core.collection.CollectionUtil;


/**
 * 分会场管理controller
 * @ClassName: PartmeetingController
 * @author: 彭浩
 * @date: 2018年3月5日 下午3:29:01
 */
@Controller
@RequestMapping("/partmeeting")
public class PartmeetingController extends BaseController {

	
	public static Logger log = LoggerFactory.getLogger(PartmeetingController.class);
	

	private static String PREFIX = "/v4ward/partmeeting/";

    @Resource
    PartmeetingSrv partmeetingSrv;

    @Resource
    SignSrv signSrv;

    /**
     * 跳转分会场管理页面
     * @Title: topartmeeting
     * @param meetingId
     * @param meetingTheme
     * @param model
     * @return
     * @return: String
     */
    @RequestMapping("")
    public String topartmeeting(@RequestParam("meetingId") String meetingId,
    		@RequestParam("meetingTheme") String meetingTheme,Model model) {

    	model.addAttribute("meetingId", meetingId);
    	model.addAttribute("meetingTheme",meetingTheme);
    	return PREFIX + "partmeeting";
    }


    /**
     * 跳转分会场参会人员页面
     * @Title: topartmeeting
     * @param meetingId
     * @param model
     * @return
     * @return: String
     */
    @RequestMapping("/toAssignUser/{meetingId}")
    public String toAssignUser(@PathVariable String meetingId, Model model) {
        Map<String,Object> params = new HashMap<>();
        params.put("partmeetingId",meetingId);
        List<PartmeetingUserPoiEntity> pupe = signSrv.listSignByPartmeetingId(params);
        JSONArray Jids = (JSONArray) JSONArray.toJSON(pupe);

        model.addAttribute("meetingId", meetingId);
        model.addAttribute("userIds", Jids);
        return PREFIX + "partmeeting_user";
    }

    /**
     * 跳转到新增分会场页面
     */
    @RequestMapping(value = "/toadd/{meetingId}")
    public String toadd(@PathVariable String meetingId,Model model) {
    	model.addAttribute("meetingId", meetingId);
        return PREFIX + "partmeeting_add";
    }

    /**
     * 查看页面
     */
	@RequestMapping(value = "/toview/{id}")
	public String toview(@PathVariable String id, Model model) {

		if (ToolUtil.isEmpty(id)) {
			throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
		}
		SysPartmeeting partmeeting = this.partmeetingSrv.selectPartmeetingById(id);

		model.addAttribute("partmeeting", partmeeting);
		return PREFIX + "partmeeting_view";
	}

    /**
    * 修改页面
    */
    @RequestMapping(value = "/tomodify/{id}")
    public String tomodify(@PathVariable String id, Model model) {
    	if (ToolUtil.isEmpty(id)) {
			throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
		}
    	SysPartmeeting partmeeting = this.partmeetingSrv.selectPartmeetingById(id);

    	model.addAttribute("partmeeting", partmeeting);
    	return PREFIX + "partmeeting_modify";
    }

    /**
     * 获取分会场列表
     * @Title: list
     * @param reuqest
     * @param response
     * @return
     * @return: Object
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam("meetingId") String meetingId,
    		@RequestParam("partmeetingTitle") String partmeetingTitle,HttpServletRequest reuqest) {

    	Map<String,Object> para = new HashMap<String,Object>();
    	para.put("meetingId", meetingId);
    	para.put("partmeetingTitle", partmeetingTitle);

    	new PageFactory<Map<String, Object>>().defaultPage();
    	List<Map<String, Object>> list = partmeetingSrv.listPartmeetings(para);

        PageInfo pageInfo = new PageInfo(list);
        Page pageList = (Page) list;
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("total",pageInfo.getTotal());
        result.put("rows", pageList);

        return result;
    }


    /**
     * 新增分会场信息
     */
    @RequestMapping(value = "/partmeetingAdd")
    @ResponseBody
    public Tip add(@RequestBody SysPartmeeting partmeeting, BindingResult result) {
        if (result.hasErrors()) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        partmeetingSrv.addPartmeeting(setCreateEmpAndTime(partmeeting));
      //分会场状态完成，会议自动完成
        partmeetingSrv.checkMeetingStatusComplete(partmeeting.getMeetingId());
        return SUCCESS_TIP;
    }



    /**
     * 删除分会场
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Tip delete(@RequestParam String partmeetingId) {
        if (ToolUtil.isEmpty(partmeetingId)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }

        SysPartmeeting partmeeting = this.partmeetingSrv.selectPartmeetingById(partmeetingId);
        if(null==partmeeting){
        	throw new BussinessException(BizExceptionEnum.DB_RESOURCE_NULL);
        }
        partmeetingSrv.deletePartmeting(partmeetingId);
      //分会场状态完成，会议自动完成
        partmeetingSrv.checkMeetingStatusComplete(partmeeting.getMeetingId());

        return SUCCESS_TIP;
    }



    /**
     * 修改分会场
     */
    @RequestMapping(value = "/partmeetingEdit")
    @ResponseBody
    public Tip edit(@RequestBody SysPartmeeting partmeeting, BindingResult result) {
        if (result.hasErrors()) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }

        partmeetingSrv.editPartmeeting(setUpdateEmpAndTime(partmeeting));
        //分会场状态完成，会议自动完成
        partmeetingSrv.checkMeetingStatusComplete(partmeeting.getMeetingId());
        return SUCCESS_TIP;
    }
    
    /**
     * 跳转到上传材料页面
     */
    @RequestMapping(value = "/toUpload/{partmeetingId}")
    public String toUpload(@PathVariable String partmeetingId,Model model) {
    	
		List<Map<String, Object>> resourcesList = partmeetingSrv
				.listResourcesByPartmeetingId(partmeetingId);
    	
    	model.addAttribute("partmeetingId", partmeetingId);
    	model.addAttribute("resourcesList", resourcesList);
        return PREFIX + "upload";
    }
	
    
    
    /** 
     * 上传会议材料
     * @Title: upload
     * @param file
     * @param request
     * @return
     * @throws IllegalStateException
     * @throws IOException
     * @return: String
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/upload")
    public String upload(@RequestParam MultipartFile file, HttpServletRequest request,
    		@RequestParam("partmeetingId") String partmeetingId) throws IllegalStateException, IOException {

        // 判断是否有文件
        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            
            try {
            	
                SysResources resources= new SysResources();
                resources.setId(UUIDUtil.getUUID());
                resources.setPartmeetingId(partmeetingId);
                resources.setResourcesTitle(fileName);
                resources.setResourcesType(fileName.substring(fileName.lastIndexOf(".")+1));
                resources.setResourcesSize((double)file.getSize());
                resources.setResourcesStatus(0);//默认 允许 不签到查看
                resources.setOrderCode(1000);//默认排序1000
                resources.setVersion(0);
            	
                //文件上传
				String stroeFilePath = UploadUtil.upload(file,
						FileConstant.PREFIX + FileConstant.SEPARATOR + FileConstant.PDF_PATHNAME,
						resources.getId() + "." + resources.getResourcesType());
				resources.setResourcesPath(stroeFilePath);
           
                partmeetingSrv.insertResources(setCreateEmpAndTime(resources));
            } catch (Exception e) {
            	log.error("文件存储失败",e);
            }
            //返回json
            return "uploadimg success";
        } else {
            System.out.println("上传失败,文件异常！");
        }
        return "success";
    }


//    public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
//        File targetFile = new File(filePath);
//        if(!targetFile.exists()){
//            targetFile.mkdirs();
//        }
//        FileOutputStream out = new FileOutputStream(filePath+fileName);
//        out.write(file);
//        out.flush();
//        out.close();
//    }
    
    
    
    /**
     * 删除材料
     */
    @RequestMapping(value = "/deleteResource")
    @ResponseBody
    public Tip deleteResource(@RequestParam String id) {
        if (ToolUtil.isEmpty(id)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }

       try {
    	   File file = new File(partmeetingSrv.getResource(id).getResourcesPath());
		   if(file.exists()){
			   file.delete();
		   }
		} catch (Exception e) {
			log.error("文件删除失败",e);
		}
       
        partmeetingSrv.deleteResourcesById(id);

        return SUCCESS_TIP;
    }
    
    /**
     * 材料修改页面
     */
     @RequestMapping(value = "/toResourceSet/{id}")
     public String toResourceSet(@PathVariable String id, Model model) {
     	if (ToolUtil.isEmpty(id)) {
 			throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
 		}
     	SysResources resource = this.partmeetingSrv.getResource(id);

     	model.addAttribute("resource", resource);
     	return PREFIX + "resource_set";
     }
     
     
     /**
      * 修改分会场
      */
     @RequestMapping(value = "/resourceSet")
     @ResponseBody
     public Tip resourceSet(@RequestBody SysResources resource, BindingResult result) {
         if (result.hasErrors()) {
             throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
         }

         partmeetingSrv.editResource(setUpdateEmpAndTime(resource));
         return SUCCESS_TIP;
     }
     
	   private static String  UTF_8 = "UTF-8";
	   private static Integer QR_WIDTH = 300;
	   private static Integer QR_HEIGHT = 300;
	   private static String QR_FILE_TYPE = "png";


    /** 
     * 生成二维码
     * @Title: downloadQr
     * @param httpServletResponse
     * @param data
     * @return: void
     */
    @RequestMapping(value = "/qrCode", method = RequestMethod.GET)
    @ResponseBody
	public void downloadQr(HttpServletResponse httpServletResponse, String data) {

		try {
			String dataHandle = new String(data.getBytes(UTF_8), UTF_8);
			dataHandle = java.net.URLEncoder.encode(dataHandle,UTF_8);
			BitMatrix bitMatrix = new MultiFormatWriter().encode(
					dataHandle, BarcodeFormat.QR_CODE, QR_WIDTH,QR_HEIGHT);

			httpServletResponse.reset();// 清空输出流

			OutputStream os = httpServletResponse.getOutputStream();// 取得输出流
			MatrixToImageWriter.writeToStream(bitMatrix, QR_FILE_TYPE, os);// 写入文件刷新

			os.flush();
			os.close();// 关闭输出流
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (WriterException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
    
    /*
     * 导入
     */
     @SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.POST,value = "/importExcel")
     @ResponseBody
     public Tip importExcel(MultipartFile file,String partmeetingId){
         //解析excel，
    	 
    	 StringBuffer emptyUsers = new StringBuffer();//记录导入数据中不存在的用户信息，返回前台提示
         try {
			List<PartmeetingUserPoiEntity> list = PoiUtils.importExcel(file,0,1,PartmeetingUserPoiEntity.class);
			 
			 if(CollectionUtil.isEmpty(list)){
				// excel数据为空，请重新导入
				 return new ErrorTip(CoreConstant.EXCEL_IS_EMPTY_CODE,CoreConstant.EXCEL_IS_EMPTY_MSG);
			 }
			 
			// 根据account 手机号去重
			List<PartmeetingUserPoiEntity> unique = list.stream()
					.collect(collectingAndThen(
							toCollection(() -> new TreeSet<>(comparingLong((PartmeetingUserPoiEntity::getAccount)))),
							ArrayList::new));
			 //新增的人员
			 List<SysPartmeetingUser> userListInsert = new ArrayList<SysPartmeetingUser>();
			 //更新的人员
			 List<SysPartmeetingUser> userListUpdate = new ArrayList<SysPartmeetingUser>();
			 SysPartmeetingUser partmeetingUser;
			 String userId;
			 
			 for (PartmeetingUserPoiEntity poiEntity : unique) {
				 userId = partmeetingSrv.selectUserIdByPhone(poiEntity.getAccount()+"");
				 //查无此人 跳过
				 if(StringUtils.isEmpty(userId)){
					 emptyUsers.append("["+poiEntity.getName()+":"+poiEntity.getAccount()+"],");
					 continue;
				 }
				 partmeetingUser = new SysPartmeetingUser();
				 partmeetingUser.setHotelInfo(poiEntity.getHotelInfo());
				 partmeetingUser.setRoomInfo(poiEntity.getRoomInfo());
				 partmeetingUser.setSeatInfo(poiEntity.getSeatInfo());
				 String partmeetingUserId = partmeetingSrv.getPartmeetingUserIdByPartmeetingIdAndUserId(
						partmeetingId,userId);
				 if(!StringUtils.isEmpty(partmeetingUserId)){
					 partmeetingUser.setId(partmeetingUserId);
					 userListUpdate.add(partmeetingUser);
				 }else{
					 partmeetingUser.setId(UUIDUtil.getUUID());
					 partmeetingUser.setUserId(userId);
					 partmeetingUser.setPartmeetingId(partmeetingId);
					 partmeetingUser.setBindStatus(0);//0:后台导入，1:扫码绑定
					 partmeetingUser.setSignStatus(0);//0:未签到；1:已签到；2:迟到；3:请假)
					 partmeetingUser.setVersion(0);
					 userListInsert.add(partmeetingUser);
				 }
			}
			 if(CollectionUtil.isNotEmpty(userListInsert)){
				 partmeetingSrv.userListInsert(userListInsert);//新增参会人员
			 }
			 if(CollectionUtil.isNotEmpty(userListUpdate)){
				 partmeetingSrv.userListUpdate(userListUpdate);//修改参会人员
			 }
		} catch (Exception e) {
			log.error("导入参会人员失败",e);
			return new ErrorTip(CoreConstant.EXCEL_IMPORT_ERROR_CODE, CoreConstant.EXCEL_IMPORT_ERROR_MSG);
		}
         
         if(emptyUsers.length()>0){
			return new ErrorTip(CoreConstant.EXCEL_ILLEGAL_USER_CODE, emptyUsers.substring(0, emptyUsers.length() - 1)
					+"以上"+ CoreConstant.EXCEL_ILLEGAL_USER_MSG + ",其他用户已导入成功");
        	 
         }else{
        	 return SUCCESS_TIP;
         }
     }

    /*
   * 手动增加参会人员
   */
     @RequestMapping(method = RequestMethod.POST,value = "/addPartmeetingUser")
     @ResponseBody
     public Tip addPartmeetingUser(String partmeetingId,String userIds,String delUsers) {
         List<String> arrayList = JSON.parseArray(userIds, String.class);
         List<String> delList = JSON.parseArray(delUsers, String.class);
         List<SysPartmeetingUser> partmeetingUserList = new ArrayList<SysPartmeetingUser>();
         if(arrayList.size() != 0){
             SysPartmeetingUser partmeetingUser;
             for (String str : arrayList) {
                 partmeetingUser = new SysPartmeetingUser();
                 partmeetingUser.setId(UUIDUtil.getUUID());
                 partmeetingUser.setUserId(str);
                 partmeetingUser.setPartmeetingId(partmeetingId);
                 partmeetingUser.setBindStatus(0);//0:后台导入，1:扫码绑定
                 partmeetingUser.setSignStatus(0);//0:未签到；1:已签到；2:迟到；3:请假)
                 partmeetingUser.setVersion(0);
                 partmeetingUserList.add(partmeetingUser);
             }
             partmeetingSrv.importExcel(partmeetingUserList);
         }
         if(delList.size() != 0){
             for (String str : delList) {
                 partmeetingSrv.delByPartmeetingIdAndUserId(partmeetingId,str);
             }
         }
         return SUCCESS_TIP;
     }
     
     
     
     /** 
     * 分会场人员导入模板
     * @Title: excelModel
     * @param response
     * @return: void
     */
    @RequestMapping("/excelModel")
     @ResponseBody
     public void excelModel(HttpServletResponse response){
         List<PartmeetingUserTemplate> list= new ArrayList<>();

         PoiUtils.exportExcel(list,null,"分会场人员导入模板",PartmeetingUserTemplate.class,"分会场人员导入模板.xls",response);
     }
}
