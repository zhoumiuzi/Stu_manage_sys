package com.zyy.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zyy.entity.Account;
import com.zyy.entity.Admin;
import com.zyy.entity.Course;
import com.zyy.entity.Student;
import com.zyy.exception.CustomException;
import com.zyy.mapper.AdminMapper;
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

    public PageInfo<Student> studentPage(Integer pagenum, Integer pagesize, Student student) {
        PageHelper.startPage(pagenum, pagesize);
        List<Student> studentList = adminMapper.selectAll(student);
        // System.out.println(courseList);
        return PageInfo.of(studentList);
    }

    /**
     * 登录
     * */
    public Account login(Account account){
        Account dbadmin = adminMapper.selectByUserName(account.getUsername());
        if(dbadmin==null){
            //用户不存在
            throw new CustomException("账号或密码错误");
        }
        if(!account.getPassword().equals(dbadmin.getPassword())){
            //密码错误
            throw new CustomException("密码错误");
        }
        //登录成功
        return dbadmin;
    }

    public Admin showData(String username) {
        Admin dbadmin = adminMapper.selectByUserName(username);
        if (dbadmin == null) {
            throw new CustomException("奇怪怎么没有信息");
        }
        return dbadmin;
    }

    public void updateData(Admin admin) {
        if(admin.getAdminid()==null){
            throw new IllegalArgumentException("传入管理员信息错误");
        }
        if(admin.getPassword()==null) {
            throw new IllegalArgumentException("传入管理员密码错误");
        }
        adminMapper.updateAdmin(admin);
    }
}
