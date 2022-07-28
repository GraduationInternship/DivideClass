package com.fouronesix.controller;

import com.fouronesix.entity.Class;
import com.fouronesix.entity.Student;
import com.fouronesix.service.ClassService;
import com.github.pagehelper.PageInfo;
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
public class ClassController {

    /*从容器内获取classService对象注入到classService属性当中*/
    @Autowired
    ClassService classService;

    /*/classIndex这个请求路径和init.json文件下的管理员管理的href的属性值保持一致*/
    @RequestMapping("/classIndex")
    public String classIndex(Model model){
        model.addAttribute("classList",classService.getClassTwo());
        System.out.println(classService.getClassTwo());
        return "class/classIndex";
    }

    /*查询所有班级数据，并返回*/
    @RequestMapping("/classAll")
    @ResponseBody
    public Map<String, Object> classAll(int page,int limit){

        /*page和limit是layui规定分页查询两个参数，参数名是不能更改的*/

        /*调用查询所有班级*/
        List<Class> classList = classService.getClass(page,limit);

        /*封装分页信息*/
        PageInfo<Class> pageInfo = new PageInfo<>(classList);

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

    /*搜索班级*/
    @RequestMapping("/searchClass")
    @ResponseBody
    public Map<String, Object>searchClass(Integer classCode, String className, Integer page, Integer limit){

        /*数据校验*/
        if (classCode != null && classCode.toString().trim().length()==0){
            classCode = null;
        }
        if (className == ""){
            className = null;
        }

        List<Class> classList = classService.searchClass(classCode,className,page,limit);

        PageInfo<Class> pageInfo = new PageInfo<>(classList);

        System.out.println("总共分为了多少页："+pageInfo.getPages());
        System.out.println("当前查询的是第几页："+pageInfo.getPageNum());
        System.out.println("每页显示多少条数据："+pageInfo.getPageSize());
        System.out.println("总共有多少条数据："+pageInfo.getTotal());
        System.out.println("当前页的数据："+pageInfo.getList());

        Map<String, Object> map = new HashMap<>();

        map.put("code",0);
        map.put("msg","");
        map.put("data",pageInfo.getList());
        map.put("count",pageInfo.getTotal());

        return map;
    }

    /*打开修改班级名称页面*/
    @RequestMapping("/queryClassByClassCode")
    public String queryClassByClassCode(Integer classCode, String className, Model model){
//        传入页面的数据，需要和classIndex.jsp中content内容一致
        model.addAttribute("className",className);
        model.addAttribute("classCode",classCode);
        return "class/updateClass";
    }

    /*修改班级名称 "code" 0成功 -1失败 1重名 2新旧一致*/
    @RequestMapping("/updateClassNameSubmit")
    @ResponseBody
    public Map<String, Object> updateClassNameSubmit(Integer classCode, String oldClassName, String newClassName){

        Map<String, Object> map = new HashMap<>();

        if (oldClassName.equals(newClassName)){
            map.put("code",2);
            map.put("msg","新旧名称一致，请重新输入");
        }else {
            int result = classService.updateClassName(classCode, oldClassName, newClassName);

            if (result == 0){
                map.put("code", 0);
            }else if (result == 1){
                map.put("msg","该班级名称已存在！");
                map.put("code", 1);
            }else {
                map.put("msg","修改失败");
                map.put("code", -1);
            }
        }
        return map;
    }

    /*删除班级*/
    @RequestMapping("/deleteClassByClassCodes")
    @ResponseBody
    public Map<String, Object> deleteClassByClassCodes(String classCodes){

        /*接受过来的字符串转化为集合*/
        String[] classCodesArray = classCodes.split(",");

        List<Integer> classCodesList = new ArrayList<>();

        for (String codes : classCodesArray) {
            classCodesList.add(Integer.parseInt(codes));
        }

        Boolean result = classService.deleteClass(classCodesList);

        Map<String, Object> map = new HashMap<>();

        if(result) {
            map.put("code",0);
        }else {
            map.put("code",-1);
        }
        return map;
    }

    /*打开添加班级的页面*/
    @RequestMapping("/classAdd")
    public String toClassAdd() {
        return "/class/classAdd";
    }

    /*添加班级*/
    @RequestMapping("/addClassSubmit")
    @ResponseBody
    public Map<String, Object> addClass(Class oneClass){

        System.out.println(oneClass);

        int result = classService.addClass(oneClass);

        Map<String, Object> map = new HashMap<>();

        if (result == 0){
            map.put("code", 0);
        }else if (result == 1){
            map.put("code", 1);
        }else {
            map.put("code",-1);
        }
        return map;
    }

    /*打开学生名单页面*/
    @RequestMapping("/queryStudentByClassCode")
    public String queryStudentByClassCode(Integer classCode, Model model){
//        传入页面的数据，需要和classIndex.jsp中content内容一致

        System.out.println("classController---queryStudentByClassCode---classCode:" + classCode);

        model.addAttribute("classCode",classCode);
        return "class/class_studentList";
    }

    /*根据班级编号查询学生，返回学生列表*/
    @RequestMapping("/class_student")
    @ResponseBody
    public Map<String, Object> class_student(Integer classCode,Integer page,Integer limit){

        /*page和limit是layui规定分页查询两个参数，参数名是不能更改的*/

        System.out.println("classController---class_student---classCode:" + classCode);

        /*调用 根据班级编号查询学生*/
        List<Student> studentList = classService.getStudentByClassCode(classCode,page,limit);

        System.out.println(studentList);

        /*封装分页信息*/
        PageInfo<Student> pageInfo = new PageInfo<>(studentList);

        System.out.println("学生表---页数："+pageInfo.getPages());
        System.out.println("学生表---当前页："+pageInfo.getPageNum());
        System.out.println("学生表---每页数据数量："+pageInfo.getPageSize());
        System.out.println("学生表---数据量"+pageInfo.getTotal());
        System.out.println("学生表---当前页数据："+pageInfo.getList());

        Map<String, Object> map = new HashMap<>();

        map.put("code",0);
        map.put("msg","");
        map.put("data",pageInfo.getList());
        map.put("count",pageInfo.getTotal());

        return map;
    }
}
