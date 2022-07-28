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

import java.util.ArrayList;
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
    /*删除用户信息*/
    @RequestMapping("/deleteAdminByIds")
    @ResponseBody
    public Map<String, Object> deleteAdminByIds(String ids){

        /*接受过来的字符串转化成集合*/
        /*1,5,11----->{1,5,11}*/
        String[] idarry = ids.split(",");
        List<Integer> idsList = new ArrayList<>();
        for (String id:idarry) {
            idsList.add(Integer.parseInt(id));
        }

        /*调用adminService实现删除*/
        Boolean result = adminService.deleteAdmin(idsList);

        Map<String, Object> map = new HashMap<>();

        if(result==true){
            map.put("code",0);
        }
        else {
            map.put("code",-1);
        }
        return map;
    }


    /*打开管理员添加页面*/
    @RequestMapping("/adminAdd")
    public String toAdminAdd(){
        return "admin/adminAdd";
    }


    @RequestMapping("/addAdminSubmit")
    @ResponseBody
    public Map<String, Object> addAdmin(Admin admin){

        System.out.println(admin);

        /*检查管理员类型是否选择,如果没有选择，就默认设置为普通管理员：0*/
        if(admin!=null&&admin.getAdminType()==-1)
        {
            admin.setAdminType(0);
        }

        /*验证用户名是否已经存在*/


        /*调用adminService的添加用户的方法*/
        int result = adminService.addAdmin(admin);

        Map<String, Object> map = new HashMap<>();

        if(result==0){
            /*添加成功*/
            map.put("code",0);
        }else if(result==1){
            /*用户名已经存在*/
            map.put("code",1);
        }else {
            /*添加失败*/
            map.put("code",-1);
        }

        return map;
    }


    /*打开修改管理员信息的页面*/
    @RequestMapping("/queryAdminById")
    public String queryAdminById(Integer id,Model model){
        model.addAttribute("id",id);
        return "admin/updateAdmin";
    }


    /*修改密码*/
    @RequestMapping("/updatePwdSubmit")
    @ResponseBody
    public Map<String, Object> updatePwdSubmit(Integer id,String oldPwd,String newPwd){

        Map<String, Object> map = new HashMap<>();
        if(newPwd.equals(oldPwd)){
            map.put("code",1);
            map.put("msg","新密码和原密码一致！不能修改！！！");
        }else {
            Boolean result = adminService.updateAdminPassword(id, newPwd, oldPwd);

            if(result==true){

                map.put("code",0);
            }
            else {
                map.put("msg","修改失败！！");
                map.put("code",-1);
            }
        }
        return map;
    }


    /*搜索用户*/
    @RequestMapping("/searchAdmin")
    @ResponseBody
    public Map<String, Object> searchAdmin(String username,Integer adminType,Integer page,Integer limit){

        /*数据校验*/
        if(username!=null&&username.trim().length()==0){
            username=null;
        }

        if(adminType==-1){
            adminType=null;
        }

        List<Admin> adminList = adminService.searchAdmin(username, adminType, page, limit);

        PageInfo<Admin> pageInfo = new PageInfo<>(adminList);

        /*layui数据表格返回数据格式*/
        Map<String, Object> map = new HashMap<>();

        map.put("code",0);
        map.put("msg","");
        map.put("data",pageInfo.getList());
        map.put("count",pageInfo.getTotal());

        return map;
    }

}
