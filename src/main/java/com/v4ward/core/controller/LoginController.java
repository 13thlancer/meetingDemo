package com.v4ward.core.controller;

import com.alibaba.fastjson.JSONArray;
import com.v4ward.core.kit.HttpKit;
import com.v4ward.core.log.LogManager;
import com.v4ward.core.log.factory.LogTaskFactory;
import com.v4ward.core.model.Manager;
import com.v4ward.core.model.Menu;
import com.v4ward.core.service.LoginSrv;
import com.v4ward.core.utils.Md5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Controller
public class LoginController extends BaseController{

    @Autowired
    private LoginSrv loginSrv;

    /**
     * 跳转到登录页面
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "/login";
    }

    /**
     * 点击登录执行的动作
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginVali(Model model, HttpServletRequest request, HttpServletResponse response) {
        String username = super.getPara("username").trim();
        String password = super.getPara("password").trim();
//        String verified = super.getPara("verified").trim();

        //从session中取当前验证码
//        String vrifyCode = (String) super.getSession().getAttribute("vrifyCode");
//        //判断验证码是否输入正确
//        if(!vrifyCode.equals(verified)){
//            model.addAttribute("tips", "验证码错误！");
//            LogManager.me().executeLog(LogTaskFactory.loginLog(username, "验证码错误", HttpKit.getIp()));
//            return "/login";
//        }
        Manager manager = loginSrv.getManager(username);
        if(manager != null){
            Boolean logFlag = checkPwd(manager,password);
            if(logFlag){
                String managerId = manager.getId();
                List<String> types = this.loginSrv.getRoleTypeList(managerId);
                String[] roleType=types.toArray(new String[types.size()]);
                //获取菜单
                List<Map<String,Object>> menus = loginSrv.getMenuList(managerId);
                JSONArray Jmenus = (JSONArray) JSONArray.toJSON(menus);
                //获取权限
                List<Menu> auths = loginSrv.getAuthList(managerId);
                HttpSession session = request.getSession(true);
                session.setAttribute("manager", manager);
                session.setAttribute("menus", Jmenus);
                session.setAttribute("auths", auths);
                session.setAttribute("roleType",roleType);

                LogManager.me().executeLog(LogTaskFactory.loginLog(manager.getId(), HttpKit.getIp()));
                return REDIRECT +"/index";
            }else{
                model.addAttribute("tips", "密码错误");
                LogManager.me().executeLog(LogTaskFactory.loginLog(username, "密码错误",HttpKit.getIp()));
                return "/login";
            }
        }else{
            model.addAttribute("tips", "用户不存在");
            LogManager.me().executeLog(LogTaskFactory.loginLog(username, "用户不存在",HttpKit.getIp()));
            return "/login";
        }
    }

    /**
     * 退出登录
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logOut(HttpServletRequest request) {
        LogManager.me().executeLog(LogTaskFactory.exitLog(getManager().getId(), HttpKit.getIp()));
        HttpSession session = request.getSession(true);
        session.removeAttribute("manager");
        session.removeAttribute("menus");
        session.invalidate();
        return REDIRECT + "/login";
    }

    public Boolean checkPwd(Manager manager, String password) {
        String salt = manager.getSalt();
        String dbPwd = manager.getPassword();
        if(dbPwd.equals(Md5Utils.encrypt(password,salt))){
            return true;
        }
        return false;
    }
}
