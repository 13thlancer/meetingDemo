package com.v4ward.core.controller;

import com.v4ward.core.annotation.Permission;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class IndexController extends BaseController{

    @RequestMapping("/")
    public String toIndex(){
        return "/index";
    }

    @RequestMapping("/index")
    public String index(){
        return "/index";
    }

    @RequestMapping("/blackboard")
    public String blackboard(){
        return "/blackboard";
    }

    @RequestMapping("/toUser")
    public String toUser(){
        return "/system/user/user";
    }

    @RequestMapping("/toRole")
    public String toRole(){
        return "/system/role/role";
    }

    @RequestMapping("/toMenu")
    public String toMenu(){
        return "/system/menu/menu";
    }

    @RequestMapping("/toOrg")
    public String toOrg(){
        return "/system/org/org";
    }

    @RequestMapping("/toTest")
    public String toTest(){
        return "/system/test/test";
    }

    @RequestMapping("/toSubTest")
    public String toSubTest(){
        return "/system/test/subTest";
    }

}
