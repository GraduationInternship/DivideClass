package com.fouronesix.controller;

import com.fouronesix.entity.Admin;
import com.fouronesix.service.AdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AdminController {

    /*从容器内获取AdminService对象注入到adminService属性当中*/
    @Autowired
    AdminService adminService;

    /*/adminIndex这个请求路径和init.json文件下的管理员管理的href的属性值保持一致*/
    @RequestMapping("/adminIndex")
    public String adminIndex(){
        return "admin/adminIndex";
    }


    /*查询所有管理员数据，并返回*/
    @RequestMapping("/adminAll")
    @ResponseBody
    public Map<String, Object> adminAll(int page,int limit){

        /*page和limit是layui规定分页查询两个参数，参数名是不能更改的*/

        /*调用查询所有管理员的*/
        List<Admin> adminList = adminService.getAdmin(page, limit);

        /*封装分页信息*/
        PageInfo<Admin> pageInfo = new PageInfo<>(adminList);

        System.out.println("总共分为了多少页："+pageInfo.getPages());
        System.out.println("当前查询的是第几页："+pageInfo.getPageNum());
        System.out.println("每页显示多少条数据："+pageInfo.getPageSize());
        System.out.println("总共有多少条数据："+pageInfo.getTotal());
        System.out.println("当前页的数据："+pageInfo.getList());

        Map<String, Object> map = new HashMap<>();

        /*layui规定使用数据表格加载动态数据：
            需要返回数据是json格式：包含数据内容如下：
                code（默认使用0);
                msg：表示一些提示;
                data:要展示的数据;
                count: 总共数据量
        */
        map.put("code",0);
        map.put("msg","");
        map.put("data",pageInfo.getList());
        map.put("count",pageInfo.getTotal());

        return map;

    }

}
