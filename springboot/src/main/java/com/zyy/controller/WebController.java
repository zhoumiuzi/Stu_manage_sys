package com.zyy.controller;

import com.zyy.common.Result;
import com.zyy.entity.Admin;
import com.zyy.service.AdminService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class WebController {

    @Resource
    private AdminService adminService;


    /**
     * 默认请求接口
     */
    @GetMapping("/")
    public Result hello() {
        return Result.success();
    }


    @PostMapping("/login")
    public Result login(@RequestBody Admin admin){
        System.out.println("Received admin: " + admin);
        Admin dbadmin = adminService.login(admin);
        return Result.success(dbadmin);
    }

}
