package com.fouronesix.service;

import com.fouronesix.dao.StudentMapper;
import com.fouronesix.entity.Class;
import com.fouronesix.entity.Student;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class StudentService {

    /*从容器当中获取adminMapper*/
    @Autowired
    StudentMapper studentMapper;

    /*展示学生信息业务*/
    public List<Student> getStudent(Integer page, Integer limit){

        /*在查询之前使用分页插件PageHelper指定从第几页查，查询多少条记录*/
        PageHelper.startPage(page,limit);
        List<Student> studentList = studentMapper.getAllStudent();
        for (Student student:studentList) {
            List<Class> classList = studentMapper.getClassNameByClassCode(student.getClassCode());
            student.setClassName(classList.get(0).getClassName());
        }

        return studentList;
    }

    /*获取班级列表*/
    public List<Class> getAllClass(){

        return studentMapper.getAllClass();
    }

    /*根据学号查询学生*/
    public List<Student> getStudentByStuNum(Integer stuNum){
        return studentMapper.getStudentByStuNum(stuNum);
    }

    /*修改学生信息*/
    public int updateStudent(Student student,Integer oldStuNum){

        /*0代表修改成功，1代表学号已存在，-1代表修改失败*/

        int i = 0;
        /*检查学号是否修改*/
        List<Student> studentList;
        if(!student.getStuNum().equals(oldStuNum))
        {
            /*验证学号是否已经存在*/
            /*(1)根据学号查询用户*/
            studentList = studentMapper.getStudentByStuNum(student.getStuNum());
            /*(2)判断数据库内是否存在和新修改学生的学号重复*/
            if(studentList!=null && studentList.size()>0){
                /*学号已经存在*/
                return 1;
            }
        }
        student.setClassName(oldStuNum.toString());/*用className来保存oldStuNum*/
        studentList = studentMapper.getStudentByStuNum(oldStuNum);
        /*是否修改班级，修改班级则更改班级人数*/
        if (!student.getClassCode().equals(studentList.get(0).getClassCode())){
            List<Integer> stuNums = new ArrayList<>();
            stuNums.add(oldStuNum);
            int r = studentMapper.reduceClassSum(stuNums);
            int a = studentMapper.addClassSum(student.getClassCode());
            if (r>0 && a>0){/*班级人数更改成功*/
                i = studentMapper.updateStudent(student);
            }
            else if(r>0){
                studentMapper.addClassSum(studentList.get(0).getClassCode());
                return -1;
            }else{
                return -1;
            }
        }else{
            i = studentMapper.updateStudent(student);
        }
        System.out.println("+++update dao result+++"+i);
        if(i > 0){
            return 0;
        }
        else {
            return -1;
        }
    }

    /*验证登录*/
    public Boolean checkLogin(Student student, HttpSession session){

        /*（1）调用adminMapper的根据用户名、密码、管理员类型查询用户*/
        Student a = studentMapper.getStudentCheckLogin(student);

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


    /*删除学生记录*/
    public Boolean deleteStudent(List<Integer> stuNums){

        int j = studentMapper.reduceClassSum(stuNums);
        int i = studentMapper.deleteStudent(stuNums);

        if(i > 0 && j > 0){
            /*表示删除成功*/
            return true;
        }
        else {
            return false;
        }

    }


    /*添加学生信息*/
    public int addStudent(Student student){

        /*0代表添加成功，1代表学生已存在，-1代表添加失败*/

        /*(1)根据学号查询用户*/
        List<Student> studentList = studentMapper.getStudentByStuNum(student.getStuNum());

        /*(2)判断数据库内是否存在和新添加学生的学号重复*/
        if(studentList!=null && studentList.size()>0){
            /*学号已经存在*/
            return 1;
        }
        else {
            /*调用studentMapper内的新增一条学生记录的方法*/
            int i = studentMapper.addStudent(student);
            int j = studentMapper.addClassSum(student.getClassCode());
            if(i>0 && j>0){
                return 0;
            }
            else if (i > 0){/*成功新增学生但无法更新班级人数*/
                List<Integer> stuNums = new ArrayList<>();
                stuNums.add(student.getStuNum());
                studentMapper.deleteStudent(stuNums);
                return -1;
            }
            else {
                return -1;
            }

        }
    }



    /*修改密码*/
    public Boolean updateAdminPassword(Integer id,String newPassword,String oldPassword){

        int i = studentMapper.updateAdminPassword(id, newPassword, oldPassword);

        if(i>0){
            return true;
        }
        else {
            return false;
        }
    }


    /*搜索用户*/
    public List<Student> searchStudent(Student student,Integer page,Integer limit){

        PageHelper.startPage(page,limit);
        List<Student> studentList = studentMapper.searchStudent(student);
        for (Student students:studentList) {
            List<Class> classList = studentMapper.getClassNameByClassCode(students.getClassCode());
            students.setClassName(classList.get(0).getClassName());
        }
        return studentList;
    }
}
