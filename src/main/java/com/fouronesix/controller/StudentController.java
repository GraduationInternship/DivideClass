package com.fouronesix.controller;

import com.fouronesix.entity.Student;
import com.fouronesix.service.StudentService;
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
public class StudentController {

    /*从容器内获取AdminService对象注入到adminService属性当中*/
    @Autowired
    StudentService studentService;

    /*/studentIndex这个请求路径和init.json文件下的管理员管理的href的属性值保持一致*/
    @RequestMapping("/studentIndex")
    public String studentIndex(Model model){
        model.addAttribute("classList",studentService.getAllClass());
//        System.out.println(studentService.getAllClass());
        return "student/studentIndex";
    }


    /*查询所有管理员数据，并返回*/
    @RequestMapping("/studentAll")
    @ResponseBody
    public Map<String, Object> studentAll(int page,int limit,Model model){

        /*page和limit是layui规定分页查询两个参数，参数名是不能更改的*/
        /*调用查询所有学生的*/
        List<Student> studentList = studentService.getStudent(page, limit);

        /*封装分页信息*/
        PageInfo<Student> pageInfo = new PageInfo<>(studentList);

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


    /*打开修改学生信息的页面*/
    @RequestMapping("/queryStudentByStuNum")
    public String queryStudentByStuNum(Integer stuNum,Model model){

        model.addAttribute("stuNum",stuNum);
        model.addAttribute("classList",studentService.getAllClass());
        model.addAttribute("info",studentService.getStudentByStuNum(stuNum).get(0));
        return "student/studentUpdate";
    }

    @RequestMapping("/updateStudentSubmit")
    @ResponseBody
    public Map<String, Object> updateStudent(Student student,Integer oldStuNum){
        System.out.println("update student submit："+student+oldStuNum);

        /*检查学号是否修改*/
        /*验证学号是否已经存在*/
        /*调用studentService的修改学生的方法*/
        int result = studentService.updateStudent(student,oldStuNum);
        System.out.println("update result"+result);

        Map<String, Object> map = new HashMap<>();
        if(result == 0){
            /*修改成功*/
            map.put("code",0);
        }else if(result == 1){
            /*学号已经存在*/
            map.put("code",1);
        }else {
            /*修改失败*/
            map.put("code",-1);
        }
        System.out.println("code:"+map.get("code"));
        return map;
    }

    /*删除用户信息*/
    @RequestMapping("/deleteStudentByStuNums")
    @ResponseBody
    public Map<String, Object> deleteStudentByStuNums(String stuNums){
        
        /*接受过来的字符串转化成集合*/
        /*1,5,11----->{1,5,11}*/
        System.out.println("stuNums："+stuNums);
        String[] stuNumArray = stuNums.split(",");
        List<Integer> stuNumsList = new ArrayList<>();
        for (String stuNum:stuNumArray) {
            stuNumsList.add(Integer.parseInt(stuNum));
        }

        /*调用studentService实现删除*/
        Boolean result = studentService.deleteStudent(stuNumsList);
        System.out.println("deleteResult"+(result?1:0));
        Map<String, Object> map = new HashMap<>();

        if(result){
            map.put("code",0);
        }
        else {
            map.put("code",-1);
        }
        return map;
    }


    /*打开学生添加页面*/
    @RequestMapping("/studentAdd")
    public String toStudentAdd(Model model){
        model.addAttribute("classList",studentService.getAllClass());
        return "student/studentAdd";
    }


    @RequestMapping("/addStudentSubmit")
    @ResponseBody
    public Map<String, Object> addStudent(Student student){

        System.out.println("add student submit："+student);
        /*验证学号是否已经存在*/
        /*调用studentService的添加学生的方法*/
        int result = studentService.addStudent(student);
        System.out.println("add.service.result"+result);
        Map<String, Object> map = new HashMap<>();

        if(result == 0){
            /*添加成功*/
            map.put("code",0);
        }else if(result == 1){
            /*学号已经存在*/
            map.put("code",1);
        }else {
            /*添加失败*/
            map.put("code",-1);
        }
        System.out.println("add.map.code"+map.get("code"));
        return map;
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
            Boolean result = studentService.updateAdminPassword(id, newPwd, oldPwd);

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
    @RequestMapping("/searchStudent")
    @ResponseBody
    public Map<String, Object> searchStudent(String stuNum,String stuName,Integer classCode,Integer page,Integer limit){

        System.out.println("search----stuName"+stuName);
        System.out.println("search----stuNum"+stuNum);
        System.out.println("search----classCode"+classCode);
        /*数据校验*/
        if(stuName!=null&&stuName.trim().length()==0){
            stuName=null;
        }

        Integer stuNumTwo;
        if(stuNum!=null&&stuNum.trim().length()==0){
            stuNumTwo=null;
        }
        else {
            stuNumTwo = Integer.parseInt(stuNum);
        }

        if(classCode==-1){
            classCode=null;
        }

        Student student = new Student();
        student.setClassCode(classCode);
        student.setStuNum(stuNumTwo);
        student.setStuName(stuName);
        System.out.println("searchStudent-----"+student);
        List<Student> studentList = studentService.searchStudent(student, page, limit);

        PageInfo<Student> pageInfo = new PageInfo<>(studentList);

        /*layui数据表格返回数据格式*/
        Map<String, Object> map = new HashMap<>();

        map.put("code",0);
        map.put("msg","");
        map.put("data",pageInfo.getList());
        map.put("count",pageInfo.getTotal());

        return map;
    }

}
