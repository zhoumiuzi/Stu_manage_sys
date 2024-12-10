package com.zyy.controller;

import cn.hutool.core.util.ObjectUtil;
import com.zyy.common.Result;
import com.zyy.common.RoleEnum;
import com.zyy.entity.Account;
import com.zyy.entity.Student;
import com.zyy.service.AdminService;
import com.zyy.service.StudentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class WebController {

    @Resource
    private AdminService adminService;

    @Resource
    private StudentService studentService;

    /**
     * 默认请求接口
     */
    @GetMapping("/")
    public Result hello() {
        return Result.success();
    }


    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        Account dbaccount;
        if (RoleEnum.Admin.name().equals(account.getRole())) {
            System.out.println("Received admin: " + account);
            dbaccount = adminService.login(account);
            return Result.success(dbaccount);
        } else if (RoleEnum.Student.name().equals(account.getRole())) {
            System.out.println("Received Student: " + account);
            dbaccount = studentService.login(account);
            return Result.success(dbaccount);
        } else return Result.error("角色错误，角色为:"+account.getRole());
    }

    @PostMapping("/register")
    public Result register(@RequestBody Account account) {
        if(ObjectUtil.hasEmpty(account.getUsername()) || ObjectUtil.hasEmpty(account.getPassword())) {
            return Result.error("账号或密码为空");
        }
        studentService.register(account);
        return Result.success("注册成功");
    }

}
