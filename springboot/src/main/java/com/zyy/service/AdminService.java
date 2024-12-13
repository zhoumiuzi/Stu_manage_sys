package com.zyy.service;

import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zyy.entity.Account;
import com.zyy.entity.Admin;
import com.zyy.entity.Student;
import com.zyy.exception.CustomException;
import com.zyy.mapper.AdminMapper;
import com.zyy.mapper.StudentMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: zhouMiuzi
 * @CreateTime: 2024-12-06
 * @Description:
 * @Version: 1.0
 */

@Service
public class AdminService {

    @Resource
    private AdminMapper adminMapper;

    @Resource
    private StudentMapper studentMapper;

    public PageInfo<Student> studentPage(Integer pagenum, Integer pagesize, Student student) {
        PageHelper.startPage(pagenum, pagesize);
        List<Student> studentList = adminMapper.selectAll(student);
        // System.out.println(courseList);
        return PageInfo.of(studentList);
    }

    /**
     * 登录
     */
    public Account login(Account account) {
        Account dbadmin = adminMapper.selectByUserName(account.getUsername());
        if (dbadmin == null) {
            //用户不存在
            throw new CustomException("账号或密码错误");
        }
        if (!account.getPassword().equals(dbadmin.getPassword())) {
            //密码错误
            throw new CustomException("密码错误");
        }
        //登录成功
        return dbadmin;
    }

    //展示管理员的个人信息
    public Admin showData(String username) {
        Admin dbadmin = adminMapper.selectByUserName(username);
        if (dbadmin == null) {
            throw new CustomException("奇怪怎么没有信息");
        }
        return dbadmin;
    }

    public void updateData(Admin admin) {
        if (admin.getAdminid() == null) {
            throw new IllegalArgumentException("传入管理员信息错误");
        }
        if (admin.getPassword() == null) {
            throw new IllegalArgumentException("传入管理员密码错误");
        }
        adminMapper.updateAdmin(admin);
    }

    //删除学生信息
    public void deleteStudentById(Integer studentid) {
        adminMapper.deleteStudentById(studentid);
    }

    //添加学生信息
    public void addStudentData(Student student) {
        if (student.getGender() == null || (!student.getGender().equals("M") && !student.getGender().equals("F"))) {
            throw new IllegalArgumentException("不合法的 性别 value");
        }
        if (studentMapper.selectByUserName(student.getUsername()) != null) {
            throw new IllegalArgumentException("学号已存在");
        }
        adminMapper.insertStudent(student);
    }

    public boolean isUsernameExists(String username) {
        return studentMapper.selectByUserName(username) != null;
    }



    public void updateStudentData(Student student) {
        if (ObjectUtil.isEmpty(student.getStudentid())) {
            throw new IllegalArgumentException("学生id为空怎么传进来的？");
        }
        adminMapper.UpdateStudent(student);
    }

    public void changeStudentPassword(Integer studentid, String newPassword) {
        Student student = studentMapper.findById(studentid);
        if (student == null) {
            throw new IllegalArgumentException("学生不存在");
        }
        student.setPassword(newPassword);
        studentMapper.updateStudentPassword(student);
    }
}
