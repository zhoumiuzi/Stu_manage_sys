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

}
