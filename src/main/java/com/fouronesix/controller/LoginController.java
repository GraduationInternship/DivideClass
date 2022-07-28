package com.fouronesix.controller;

import com.fouronesix.entity.Admin;
import com.fouronesix.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    AdminService adminService;

    @RequestMapping("/toLogin")
    public String toLogin()
    {
        return "login";
    }

    /*登录*/
    @RequestMapping("/login")
    public String login(Admin admin, HttpSession session, Model model){

        /*（1）直接调用adminService验证填写登录信息*/
        Boolean result = adminService.checkLogin(admin, session);

        if(result==true){
            return "index";
        }
        else {
            model.addAttribute("msg","用户名、密码或管理员类型填写选择错误！！");
            return "login";
        }
    }

    /*/welcome和api文件夹下的init.json文件的首页的href指向的值保持一致*/
    @RequestMapping("/welcome")
    public String welcome(){
        return "welcome";
    }
}
