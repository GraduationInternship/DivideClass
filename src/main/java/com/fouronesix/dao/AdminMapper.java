package com.fouronesix.dao;


import com.fouronesix.entity.Admin;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AdminMapper {

    /*查询所有管理员信息*/
    List<Admin> getAllAdmin();

    @Select("select * from admin where username=#{username} and password=#{password} and adminType=#{adminType}")
    /*通过用户名、密码、管理员类型*/
    Admin getAdminCheckLogin(Admin admin);

}
