package com.zyy.controller;

import com.github.pagehelper.PageInfo;
import com.zyy.common.Result;
import com.zyy.entity.Admin;
import com.zyy.entity.Student;
import com.zyy.exception.CustomException;
import com.zyy.service.AdminService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

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

    //添加学生信息
    @PostMapping("/studentAdd")
    public Result studentAddSave(@RequestBody Student student) {
        try {
            if (student.getGender() == null || (!student.getGender().equals("M") && !student.getGender().equals("F"))) {
                throw new IllegalArgumentException("没有选择性别！ " + student.getGender());
            }
            adminService.addStudentData(student);
            return Result.success("学生信息：" + student + " 添加成功");
        } catch (DuplicateKeyException e) { // 捕获数据库唯一约束异常
            return Result.error("学号已存在，请输入唯一的学号");
        } catch (Exception e) {
            return Result.error("新增失败：" + e.getMessage());
        }
    }

    @PutMapping("/studentUpdate")
    public Result studentUpdate(@RequestBody Student student) {
        try {
            adminService.updateStudentData(student);
            return Result.success("学生信息修改成功");
        } catch (IllegalArgumentException e) {
            return Result.error("修改失败: " + e.getMessage());
        }
    }

    //删除学生信息
    @DeleteMapping("/delete/{studentid}")
    public Result delete(@PathVariable Integer studentid) {
        try {
            adminService.deleteStudentById(studentid);
            return Result.success("学生" + studentid + "删除成功");
        } catch (IllegalArgumentException e) {
            return Result.error("删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/changePassword/{studentid}")
    public Result changePassword(@PathVariable Integer studentid, @RequestBody Map<String, String> request) {
        String newPassword = request.get("newPassword");
        if (newPassword == null || newPassword.isEmpty()) {
            return Result.error("新密码不能为空");
        }
        adminService.changeStudentPassword(studentid, newPassword);
        return Result.success("密码修改成功");
    }

    @GetMapping("/checkUsername")
    public Result checkUsername(@RequestParam String username) {
        boolean exists = adminService.isUsernameExists(username);
        return Result.success(Map.of("exists", exists));
    }


}
