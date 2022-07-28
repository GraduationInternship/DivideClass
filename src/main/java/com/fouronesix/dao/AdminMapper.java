package com.fouronesix.dao;


import com.fouronesix.entity.Admin;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface AdminMapper {

    /*1查询所有管理员信息*/
    List<Admin> getAllAdmin();

    @Select("select * from admin where username=#{username} and password=#{password} and adminType=#{adminType}")
        /*2.通过用户名、密码、管理员类型*/
    Admin getAdminCheckLogin(Admin admin);
    /*3.删除管理员信息：批量，单个*/
    int deleteAdmin(List<Integer> ids);

    @Insert("insert into admin(username,password,adminType) values(#{username},#{password},#{adminType})")
        /*4.添加用户信息*/
    int addAdmin(Admin admin);


    @Select("select * from admin where username=#{username}")
        /*5. 根据用户名查询用户*/
    List<Admin> getAdminByName(String username);


    @Update("update admin set password=#{newPwd} where id=#{id} and password=#{oldPwd}")
        /*6.修改管理员的密码*/
    int updateAdminPassword(@Param("id") Integer id,@Param("newPwd") String newPassword,@Param("oldPwd") String oldPassword);


    /*7.搜索用户*/
    List<Admin> searchAdmin(@Param("username") String username,@Param("adminType") Integer adminType);

}
