package com.v4ward.meeting.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("/upload")
public class UploadController {

    String rootPath = "/upload/";

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, path = "/upload")
    public String upload(MultipartFile file, HttpServletRequest request) throws IllegalStateException, IOException {

        // 判断是否有文件
        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            String filePath = request.getSession().getServletContext().getRealPath("imgupload/");
            try {
                uploadFile(file.getBytes(), filePath, fileName);
            } catch (Exception e) {
                // TODO: handle exception
            }
            System.out.printf(filePath);
            //返回json
            return "uploadimg success";
        } else {
            System.out.println("上传失败！！");
        }
        return "success";
    }


    public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath+fileName);
        out.write(file);
        out.flush();
        out.close();
    }
}
