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
    /*删除用户记录*/
    public Boolean deleteAdmin(List<Integer> ids){

        int i = adminMapper.deleteAdmin(ids);

        if(i>0){
            /*表示删除成功*/
            return true;
        }
        else {
            return false;
        }

    }


    /*添加用户信息*/
    public int addAdmin(Admin admin){

        /*0代表添加成功，1代表用户名已存在，-1代表添加失败*/

        /*(1)根据用户名查询用户*/
        List<Admin> adminList = adminMapper.getAdminByName(admin.getUsername());

        /*(2)判断数据库内是否存在和新添加用户的用户名重复*/
        if(adminList!=null&&adminList.size()>0){
            /*用户名已经存在*/
            return 1;
        }
        else {
            /*调用adminMapper内的新增一条用户记录的方法*/
            int i = adminMapper.addAdmin(admin);
            if(i>0){
                return 0;
            }
            else {
                return -1;
            }

        }
    }



    /*修改密码*/
    public Boolean updateAdminPassword(Integer id,String newPassword,String oldPassword){

        int i = adminMapper.updateAdminPassword(id, newPassword, oldPassword);

        if(i>0){
            return true;
        }
        else {
            return false;
        }
    }


    /*搜索用户*/
    public List<Admin> searchAdmin(String username,Integer adminType,Integer page,Integer limit){

        PageHelper.startPage(page,limit);
        return adminMapper.searchAdmin(username, adminType);
    }
}
