package com.zyy.service;

import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zyy.entity.Account;
import com.zyy.entity.Student;
import com.zyy.exception.CustomException;
import com.zyy.mapper.StudentMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: zhouMiuzi
 * @CreateTime: 2024-12-08
 * @Description: 学生
 * @Version: 1.0
 */

@Service
public class StudentService {

    @Resource
    private StudentMapper studentMapper;

    public Account login(Account account){
        Account dbstudent = studentMapper.selectByUserName(account.getUsername());
        if(dbstudent==null){
            //用户不存在
            throw new CustomException("账号或密码错误");
        }
        if(!account.getPassword().equals(dbstudent.getPassword())){
            //密码错误
            throw new CustomException("密码错误");
        }
        //登录成功
        return dbstudent;
    }

    public PageInfo<Student> selectPage(Integer pagenum, Integer pagesize, Student student) {
        PageHelper.startPage(pagenum, pagesize);
        List<Student> studentList=studentMapper.selectAll(student);;
        System.out.println("Query Parameters: username=" + student.getUsername() + ", name=" + student.getName());
        return PageInfo.of(studentList);

    }

    //学生注册
    public void register(Account account) {
        Student student = new Student();
        student.setUsername(account.getUsername());
        student.setPassword(account.getPassword());
        student.setName(account.getName());
        student.setRole("Student");
        this.add(student);
    }

    //学生新增
    public void add(Student student) {
        Student dbstudent = studentMapper.selectByUserName(student.getUsername());
        if(dbstudent!=null){
            throw new CustomException("账号已存在");
        }
        if(ObjectUtil.isEmpty(student.getName())){
            student.setUsername("momo");
        }
        student.setAge(18);
        student.setGender("F");
        student.setPhonenum("012345678910");
        student.setRole("Student");
        studentMapper.insertStudent(student);
    }

    public Student getStudentByUsername(String username) {
        Student student = studentMapper.selectByUserName(username);
        if (student == null) {
            throw new CustomException("未找到对应学生信息");
        }
        return student;
    }

    public void updateStudentPassword(String username, String oldPassword, String newPassword) {
        Student dbStudent = studentMapper.selectByUserName(username);
        if (dbStudent == null) {
            throw new IllegalArgumentException("学生信息不存在");
        }
        if (!dbStudent.getPassword().equals(oldPassword)) {
            throw new IllegalArgumentException("旧密码不正确");
        }
        if (oldPassword.equals(newPassword)) {
            throw new IllegalArgumentException("新密码不能与旧密码相同");
        }
        dbStudent.setPassword(newPassword);
        studentMapper.updateStudentPassword(dbStudent);
    }

    public void updateStudentData(Student student) {
        studentMapper.updateStudent(student);
    }

    // 删除学生，同时级联删除选课记录
    public void deleteStudent(int studentId) {

        studentMapper.deleteByStudentId(studentId);
    }


}
