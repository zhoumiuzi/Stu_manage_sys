package com.zyy.controller;

import com.github.pagehelper.PageInfo;
import com.zyy.common.Result;
import com.zyy.entity.Admin;
import com.zyy.entity.Student;
import com.zyy.exception.CustomException;
import com.zyy.service.AdminService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author: zhouMiuzi
 * @CreateTime: 2024-12-09
 * @Description:
 * @Version: 1.0
 */

@RestController
@RequestMapping("/management/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    @GetMapping("/showinfo")
    public Result showinfo(@RequestParam String username) {
        try {
            Admin dbadmin = adminService.showData(username);
            return Result.success(dbadmin);
        } catch (CustomException e) {
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    @PostMapping("/update")
    public Result update(@RequestBody Admin admin) {
        try {
            adminService.updateData(admin);
            return Result.success("管理员信息修改成功");
        } catch (IllegalArgumentException e) {
            return Result.error("管理员信息修改失败: " + e.getMessage());
        }
    }

    @GetMapping("/studentPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pagenum, @RequestParam(defaultValue = "10") Integer pagesize, Student student) {
        //需要service去返回分页数据
        PageInfo<Student> studentPageInfo = adminService.studentPage(pagenum, pagesize, student);
        return Result.success(studentPageInfo);
    }


}
