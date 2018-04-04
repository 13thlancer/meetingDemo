package com.v4ward.core.aop;


import com.alibaba.fastjson.JSON;
import com.v4ward.core.exception.*;
import com.v4ward.core.log.LogManager;
import com.v4ward.core.log.factory.LogTaskFactory;
import com.v4ward.core.model.Manager;
import com.v4ward.core.service.ManagerSrv;
import com.v4ward.core.tips.ErrorTip;
import com.v4ward.core.utils.jwt.JwtProperties;
import com.v4ward.core.utils.jwt.JwtTokenUtil;
import com.v4ward.core.kit.HttpKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.naming.AuthenticationException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;

import static com.v4ward.core.kit.HttpKit.getIp;
import static com.v4ward.core.kit.HttpKit.getRequest;

/**
 *
 * @Description: 全局的的异常拦截器（拦截所有的控制器）（带有@RequestMapping注解的方法上都会拦截）
 *
 * @ClassName: GlobalExceptionHandler
 *
 * @author huangxiang
 * @date 2017/12/25 17:37
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger log = LoggerFactory.getLogger(this.getClass());
    
	@Autowired
	private JwtProperties jwtProperties;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
    private ManagerSrv managerSrv;

    /**
     *
     * @Description: 拦截业务异常
     *
     * @date 2017/12/25 17:39
     */
    @ExceptionHandler(BussinessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String notFound(HttpServletRequest request,HttpServletResponse response,  BussinessException e){
        //LogManager.me().executeLog(LogTaskFactory.exceptionLog(((Manager)HttpKit.getSession().getAttribute("manager")).getId(),e));
        /*getRequest().setAttribute("tip", e.getMessage());*/

        Manager manager = (Manager)HttpKit.getSession().getAttribute("manager");
        String currentUserName = manager.getAccount();

        log.error("--------------全局异常日志记录开始----------------------");
        log.error("URL : " + request.getRequestURL().toString());
        log.error("HTTP请求方法 : " + request.getMethod());
        log.error("IP : " + request.getRemoteAddr());
        log.error("当前操作人员 : " + currentUserName);
        log.error("异常类型:" + e.getClass().getName());
        log.error("异常信息:" + e.getMessage());
        log.error("详细的异常堆栈:", e);
        log.error("--------------全局异常日志记录结束----------------------");

        if (request.getHeader("accept").indexOf("application/json") > -1
                || (request.getHeader("X-Requested-With")!= null && request.getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1)) {

            try {
                ServletOutputStream os = response.getOutputStream();
                ErrorTip tip = new ErrorTip(e.getCode(),e.getMessage(),e.getMessage());
                String result =  JSON.toJSONString(tip);
                os.write(result.getBytes());

            } catch (IOException e1) {
            	log.error("详细的异常堆栈:"+"_"+e1.getMessage(), e1);
            }

            return null;
        }else{
            return "/500";
        }
    }

    /**
     *
     * @Description: 用户未登录
     *
     * @date 2017/12/25 18:29
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String unAuth(AuthenticationException e){
        log.error("用户未登陆：", e);
        return "/login";
    }


    /***
     *
     * @Description: 账号被冻结
     *
     * @date 2017/12/26 9:22
     */
    @ExceptionHandler(DisabledAccountException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String accountLocked(DisabledAccountException e,Model model){
        String username = getRequest().getParameter("username");
        LogManager.me().executeLog(LogTaskFactory.loginLog(username, "账号被冻结", getIp()));
        model.addAttribute("tips", "账号被冻结");
        return "/login";
    }

    /***
     *
     * @Description: 账号密码错误
     *
     * @date 2017/12/26 9:24
     */
    @ExceptionHandler(CredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String credentials(CredentialsException e, Model model){
        String username = getRequest().getParameter("username");
        LogManager.me().executeLog(LogTaskFactory.loginLog(username, "账号密码错误", getIp()));
        model.addAttribute("tips", "账号密码错误");
        return "/login";    }

    /**
     * @Description: 无权访问该资源
     *
     * @date 2017/12/26 9:26
     */
    @ExceptionHandler(UndeclaredThrowableException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String credentials(HttpServletRequest request,HttpServletResponse response,  UndeclaredThrowableException e){
        /*getRequest().setAttribute("tip", "权限异常");*/

        Manager manager = (Manager)HttpKit.getSession().getAttribute("manager");
        String currentUserName = manager.getAccount();

        log.error("--------------全局异常日志记录开始----------------------");
        log.error("URL : " + request.getRequestURL().toString());
        log.error("HTTP请求方法 : " + request.getMethod());
        log.error("IP : " + request.getRemoteAddr());
        log.error("当前操作人员 : " + currentUserName);
        log.error("异常类型:" + e.getClass().getName());
        log.error("异常信息:" + e.getMessage());
        log.error("详细的异常堆栈:", e);
        log.error("--------------全局异常日志记录结束----------------------");

        if (request.getHeader("accept").indexOf("application/json") > -1
                || (request.getHeader("X-Requested-With")!= null && request.getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1)) {

            try {
                ServletOutputStream os = response.getOutputStream();
                ErrorTip tip = new ErrorTip(BizExceptionEnum.NO_PERMITION.getCode(),BizExceptionEnum.NO_PERMITION.getMessage() , BizExceptionEnum.NO_PERMITION.getMessage());
                String result =  JSON.toJSONString(tip);
                os.write(result.getBytes());

            } catch (IOException e1) {
            	log.error("详细的异常堆栈:"+"_"+e1.getMessage(), e1);
            }

            return null;
        }else{
            return "/403";
        }
    }


    /**
     *
     * @Description: 拦截未知的运行时异常
     *
     * @date 2017/12/26 9:27
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String notFound(HttpServletRequest request, HttpServletResponse response, RuntimeException e){
    	
    	final String clientHeader = request.getHeader(jwtProperties.getClientHeader());
    	String servletPath = request.getServletPath();
    	
    	Manager manager = null;
    	String currentUserName =null;
    	if(clientHeader == null || clientHeader.equals("")) {
    	    
    	   // LogManager.me().executeLog(LogTaskFactory.exceptionLog(((Manager)HttpKit.getSession().getAttribute("manager")).getId(), e));
    	    manager = (Manager)HttpKit.getSession().getAttribute("manager");
    	    currentUserName = manager.getAccount();
            
    	}
    	else if(servletPath.indexOf(jwtProperties.getAuthPath()) < 0) {
    		
    		 /*String token = request.getHeader(jwtProperties.getTokenHeader());
    	     String userId = jwtTokenUtil.getUsernameFromToken(token);
    	     LogManager.me().executeLog(LogTaskFactory.exceptionLog(userId, e));
    	     manager = this.managerSrv.getByUserId(userId);
    	     currentUserName = manager.getAccount();*/
    		
    	}
    	
       
        /*getRequest().setAttribute("tip", "服务器未知运行时异常");*/

        log.error("--------------全局异常日志记录开始----------------------");
        log.error("URL : " + request.getRequestURL().toString());
        log.error("HTTP请求方法 : " + request.getMethod());
        log.error("IP : " + request.getRemoteAddr());
        log.error("当前操作人员 : " + currentUserName);        
        log.error("异常类型:" + e.getClass().getName());
        log.error("异常信息:" + e.getMessage());
        log.error("详细的异常堆栈:", e);
        log.error("--------------全局异常日志记录结束----------------------");

        if (request.getHeader("accept").indexOf("application/json") > -1
                || (request.getHeader("X-Requested-With")!= null && request.getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1)) {

            try {
                ServletOutputStream os = response.getOutputStream();
                ErrorTip tip = new ErrorTip(BizExceptionEnum.SERVER_ERROR.getCode(),BizExceptionEnum.SERVER_ERROR.getMessage() , BizExceptionEnum.SERVER_ERROR.getMessage());
                String result =  JSON.toJSONString(tip);
                os.write(result.getBytes());

            } catch (IOException e1) {
            	log.error("详细的异常堆栈:"+"_"+e1.getMessage(), e1);
            }

            return null;
        }else{
            return "/500";
        }
    }

    /**
     *
     * @Description: session失效的异常拦截
     *
     * @date 2017/12/26 9:29
     */
    @ExceptionHandler(InvalidSessionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String sessionTimeout(InvalidSessionException e, Model model, HttpServletRequest request, HttpServletResponse response){
        model.addAttribute("tips", "session超时");
        assertAjax(request, response);
        return "/login";
    }
    
    /**
     *
     * @Description: session异常
     *
     * @date 2017/12/26 9:35
     */
    @ExceptionHandler(UnknownSessionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String sessionTimeout(UnknownSessionException e, Model model, HttpServletRequest request, HttpServletResponse response) {
        model.addAttribute("tips", "session超时");
        assertAjax(request, response);
        return "/login";
    }

    private void assertAjax(HttpServletRequest request, HttpServletResponse response) {
        if (request.getHeader("x-requested-with") != null
                && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
            //如果是ajax请求响应头会有，x-requested-with
            response.setHeader("sessionstatus", "timeout");//在响应头设置session状态
        }
    }

}
