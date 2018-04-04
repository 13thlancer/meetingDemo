package com.v4ward.core.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.v4ward.core.utils.DateTime;
import com.v4ward.core.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.v4ward.core.common.FileConstant;
import com.v4ward.core.common.RestConstant;
import com.v4ward.core.model.Manager;
import com.v4ward.core.model.RestParams;
import com.v4ward.core.service.ManagerSrv;
import com.v4ward.core.utils.UploadUtil;
import com.v4ward.core.utils.jwt.JwtProperties;
import com.v4ward.core.utils.jwt.JwtTokenUtil;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
@RequestMapping("/rest/settings")
public class SettingRestController extends BaseController {

    @Autowired
    private ManagerSrv managerSrv;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @ResponseBody
    @RequestMapping(value = "/personInfo", method = RequestMethod.POST)
    @ApiOperation(value="移动端读取头像", notes="读取头像")
    @ApiImplicitParam(name = "request", value = "HttpServletRequest", required = true, dataType = "HttpServletRequest")
    public Object setting(HttpServletRequest request){

        String token = request.getHeader(jwtProperties.getTokenHeader());
        String userId = jwtTokenUtil.getUsernameFromToken(token);

        Manager manager = this.managerSrv.getByUserId(userId);
        String url = manager.getUrl();
        return RestParams.newInstance(RestConstant.SUCCESS_CODE,RestConstant.GET_PIC_SUCCESS,manager);
    }


    @ResponseBody
    @RequestMapping(value = "/portraitUpload", method = RequestMethod.POST)
    @ApiOperation(value="移动端修改头像", notes="修改头像")
    @ApiImplicitParam(name = "request", value = "HttpServletRequest", required = true, dataType = "HttpServletRequest")
    public Object upload(MultipartHttpServletRequest req, HttpServletRequest request) {

        String filePath = null;

        String token = request.getHeader(jwtProperties.getTokenHeader());
        String userId = jwtTokenUtil.getUsernameFromToken(token);

        Map<String, MultipartFile> mapfile = req.getFileMap();


        for (Map.Entry<String, MultipartFile> ent : mapfile.entrySet()) {
            MultipartFile mf = ent.getValue();

            try {
                // 文件保存路径
                filePath = UploadUtil.upload(mf, FileConstant.PREFIX + FileConstant.SEPARATOR + FileConstant.IMAGE_PATHNAME,
                        userId + ".jpg");

                this.managerSrv.updateHeadImg(userId, filePath);


                } catch (Exception e) {
                    e.printStackTrace();
                    return RestParams.newInstance(RestConstant.Failure_CODE, RestConstant.UPLOAD_IMG_ERROR_DESC, null);
                }
                return RestParams.newInstance(RestConstant.SUCCESS_CODE, RestConstant.UPLOAD_IMG_SUCCESS_DESC, filePath);
            }
            return RestParams.newInstance(RestConstant.SUCCESS_CODE, RestConstant.UPLOAD_IMG_IS_EMPTY_DESC, null);

        }




        // 判断文件是否为空
//        if (!file.isEmpty()) {
//            try {
//            	// 文件保存路径
//				filePath = UploadUtil.upload(file, FileConstant.PREFIX + File.separator + FileConstant.IMAGE_PATHNAME,
//						userId + ".jpg");
//                this.managerSrv.updateHeadImg(userId,filePath);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                return RestParams.newInstance(RestConstant.Failure_CODE,RestConstant.UPLOAD_IMG_ERROR_DESC,null);
//            }
//            return RestParams.newInstance(RestConstant.SUCCESS_CODE,RestConstant.UPLOAD_IMG_SUCCESS_DESC,filePath);
//        }
//        return RestParams.newInstance(RestConstant.SUCCESS_CODE,RestConstant.UPLOAD_IMG_IS_EMPTY_DESC,null);
//    }
}
