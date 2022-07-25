package com.fouronesix.service;

import com.fouronesix.dao.AdminMapper;
import com.fouronesix.entity.Admin;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class AdminService {

    /*从容器当中获取adminMapper*/
    @Autowired
    AdminMapper adminMapper;

    /*展示管理员信息业务*/
    public List<Admin> getAdmin(Integer page, Integer limit){

        /*在查询之前使用分页插件PageHelper指定从第几页查，查询多少条记录*/
        PageHelper.startPage(page,limit);
        List<Admin> adminList = adminMapper.getAllAdmin();

        return adminList;
    }


    /*验证登录*/
    public Boolean checkLogin(Admin admin, HttpSession session){

        /*（1）调用adminMapper的根据用户名、密码、管理员类型查询用户*/
        Admin a = adminMapper.getAdminCheckLogin(admin);

        /*(2) 判断查询出来的a是否为空，如果不为空，说明填写用户名、密码、管理员类型和数据库匹配。正确，可以登录*/
        if(a!=null){
            /*(3) 如果登录成功，将登录的管理员信息放到session内*/
            session.setAttribute("LoginAdmin",a);
            return true;
        }
        else {
            return false;
        }

    }
}
