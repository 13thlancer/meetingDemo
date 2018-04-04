//package com.v4ward.core.controller;
//
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.MultiFormatWriter;
//import com.google.zxing.WriterException;
//import com.google.zxing.client.j2se.MatrixToImageWriter;
//import com.google.zxing.common.BitMatrix;
//import com.v4ward.core.poiEntity.ManagerPoiEntity;
//import com.v4ward.core.service.ManagerSrv;
//import com.v4ward.core.tips.Tip;
//import com.v4ward.core.utils.poi.PoiUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.io.UnsupportedEncodingException;
//import java.util.List;
//
//@Controller
//@RequestMapping("/test")
//public class TestController extends BaseController{
//
//    private static String  UTF_8 = "UTF-8";
//    private static Integer QR_WIDTH = 300;
//    private static Integer QR_HEIGHT = 300;
//    private static String QR_FILE_TYPE = "png";
//
//
//    @Autowired
//    private ManagerSrv managerSrv;
//
//
//    @Autowired
//    private JavaMailSender mailSender; //自动注入的Bean
//
//    @Value("${spring.mail.username}")
//    private String Sender; //读取配置文件中的参数
//
//
//    @RequestMapping(value = "/sendMail", method = RequestMethod.POST)
//    @ResponseBody
//    public Tip sendMail(String title,String content){
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom(Sender);
//        message.setTo("xxxxxxxx@qq.com"); //自己给自己发送邮件
//        message.setSubject(title);
//        message.setText(content);
//        mailSender.send(message);
//
//        return SUCCESS_TIP;
//    }
//
//
//    @RequestMapping(value = "/qrCode", method = RequestMethod.GET)
//    @ResponseBody
//    public void downloadQr(HttpServletResponse httpServletResponse, String data) {
//
//        try {
//            String dataHandle = new String(data.getBytes(UTF_8), UTF_8);
//
//        BitMatrix bitMatrix = new MultiFormatWriter().encode(dataHandle, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT);
//
//        httpServletResponse.reset();//清空输出流
//
//        OutputStream os = httpServletResponse.getOutputStream();//取得输出流
//        MatrixToImageWriter.writeToStream(bitMatrix, QR_FILE_TYPE, os);//写入文件刷新
//
//        os.flush();
//        os.close();//关闭输出流
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (WriterException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @RequestMapping("/export")
//    @ResponseBody
//    public void export(HttpServletResponse response){
//        //模拟从数据库获取需要导出的数据
//        List<ManagerPoiEntity> personList = managerSrv.listManager();
//        //导出
//        PoiUtils.exportExcel(personList,"管理员","管理员A",ManagerPoiEntity.class,"管理员.xls",response);
//    }
//
//    @RequestMapping(method = RequestMethod.POST,value = "/importExcel")
//    @ResponseBody
//    public Tip importExcel(MultipartFile file){
//        //解析excel，
//        List<ManagerPoiEntity> personList = PoiUtils.importExcel(file,2,1,ManagerPoiEntity.class);
//        for (ManagerPoiEntity manager:personList){
//            System.out.println(manager.toString());
//        }
//        //也可以使用MultipartFile,使用 FileUtil.importExcel(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> pojoClass)导入
//        System.out.println("导入数据一共【"+personList.size()+"】行");
//
//        //TODO 保存数据库
//
//
//        return SUCCESS_TIP;
//    }
//}
