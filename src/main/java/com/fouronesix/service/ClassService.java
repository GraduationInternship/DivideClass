package com.fouronesix.service;

import com.fouronesix.dao.ClassMapper;
import com.fouronesix.entity.Class;
import com.fouronesix.entity.Student;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassService {

    /*从容器中获取ClassMapper*/
    @Autowired
    ClassMapper classMapper;

    public List<Class> getClassTwo(){return classMapper.getAllClass();}

    /*展示班级信息业务*/
    public List<Class> getClass(Integer page, Integer limit){

        /*在查询之前使用分页插件PageHelper指定从第几页查，查询多少条记录*/
        PageHelper.startPage(page,limit);
        List<Class> classList = classMapper.getAllClass();

        return classList;
    }

    /*搜索班级*/
    public List<Class> searchClass(Integer classCode, String className, Integer page, Integer limit){

        PageHelper.startPage(page,limit);
        return classMapper.searchClass(classCode, className);
    }

    /*修改班级名称 返回 0成功 -1失败 1重名*/
    public int updateClassName(Integer classCode, String oldClassName, String newClassName){

        /*根据班级编号或班级名称查找班级*/
        List<Class> sameName_classList = classMapper.getClassByName(newClassName);

        System.out.println("班级编号："+classCode+" 旧名称："+oldClassName+" 新名称："+newClassName);
        System.out.println("there is service:updateClassName-----"+sameName_classList);

        // 防止班级重名
        if (sameName_classList != null && sameName_classList.size()>0){
            return 1;
        }else {
            int i = classMapper.updateClassName(classCode, oldClassName, newClassName);

            if (i > 0){
                return 0;
            }else {
                return -1;
            }
        }
    }

    /*删除班级*/
    public Boolean deleteClass(List<Integer> classCodes){

        int i = classMapper.deleteClass(classCodes);

        if (i > 0){
            return true;
        }else {
            return false;
        }
    }

    /*添加班级*/
    public int addClass(Class oneClass){

        /*根据班级编号或班级名称查找班级*/
        List<Class> classList = classMapper.getClassByCodeORName(oneClass.getClassCode(), oneClass.getClassName());

        /*判断是否重复*/
        if (classList != null && classList.size()>0){
            // 已重复
            return 1;
        }else {
            int i = classMapper.addClass(oneClass);
            if (i > 0){
                return 0;   // success
            }else {
                return -1;  // fail
            }
        }
    }

    /*根据班级编号查找学生*/
    public List<Student> getStudentByClassCode(Integer classCode, Integer page, Integer limit) {

        PageHelper.startPage(page,limit);

        System.out.println("classService---getStudentByClassCode---classCode:"+classCode);

        return classMapper.getStudentByClassCode(classCode);
    }
}
