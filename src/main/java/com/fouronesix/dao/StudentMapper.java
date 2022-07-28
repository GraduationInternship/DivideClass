package com.fouronesix.dao;


import com.fouronesix.entity.Class;
import com.fouronesix.entity.Student;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface StudentMapper {

    /*1.查询所有学生信息*/
    List<Student> getAllStudent();

    @Select("select * from student where stuNum=#{username} and password=#{password}")
    /*2.通过学号、密码查询用户*/
    Student getStudentCheckLogin(Student student);


    /*3.删除学生信息：批量，单个*/
    int deleteStudent(List<Integer> stuNums);


    @Insert("insert into student(stuNum,stuName,password,sex,classCode) values(#{stuNum},#{stuName},#{password},#{sex},#{classCode})")
    /*4.添加学生信息*/
    int addStudent(Student student);


    @Select("select * from student where stuNum=#{stuNum}")
    /*5. 根据学号查询学生*/
    List<Student> getStudentByStuNum(Integer stuNum);

    @Select("select * from class where classCode=#{classCode}")
    /*6. 根据班号查询班级名*/
    List<Class> getClassNameByClassCode(Integer classCode);

    /*7.查询所有班级信息*/
    @Select("select * from class")
    List<Class> getAllClass();

    /*8.增加班级人数*/
    @Update("update class set sum=sum+1 where classCode=#{classCode}")
    int addClassSum(Integer classCode);

    /*9.根据学生学号减少班级人数*/
    int reduceClassSum(List<Integer>  stuNums);

    int updateStudent(Student student);

    @Update("update admin set password=#{newPwd} where id=#{id} and password=#{oldPwd}")
    /*6.修改管理员的密码*/
    int updateAdminPassword(@Param("id") Integer id, @Param("newPwd") String newPassword, @Param("oldPwd") String oldPassword);


    /*7.搜索用户*/
    List<Student> searchStudent(Student student);

}
