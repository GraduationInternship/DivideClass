package com.fouronesix.dao;

import com.fouronesix.entity.Class;
import com.fouronesix.entity.Student;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ClassMapper {

    /*查询所有班级信息*/
    @Select("select * from class")
    List<Class> getAllClass();

    /*搜索班级,toolbar搜索*/
    List<Class> searchClass(@Param("classCode") Integer classCode, @Param("className") String className);

    /*修改班级名称*/
    @Update("update class set className=#{newClassName} where classCode=#{classCode}")
    int updateClassName(@Param("classCode") Integer classCode, @Param("oldClassName") String oldClassName, @Param("newClassName") String newClassName);

    /*删除班级：批量、单个*/
    int deleteClass(List<Integer> classCodes);

    /*添加班级*/
    @Insert("insert into class(classCode,className) values(#{classCode},#{className})")
    int addClass(Class oneClass);

    /*根据班级编号或班级名称查找班级*/
    @Select("select * from class where classCode=#{classCode} or className=#{className}")
    List<Class> getClassByCodeORName(@Param("classCode") Integer classCode, @Param("className") String className);

    /*根据班级名称和班级名称查找班级*/
    @Select("select * from class where className=#{className}")
    List<Class> getClassByName(@Param("className") String className);

    /*根据班级编号查找学生*/
    public List<Student> getStudentByClassCode(@Param("classCode") Integer classCode);
}
