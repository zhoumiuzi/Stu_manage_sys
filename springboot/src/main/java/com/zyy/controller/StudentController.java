package com.zyy.controller;

import com.github.pagehelper.PageInfo;
import com.zyy.common.Result;
import com.zyy.entity.Password;
import com.zyy.entity.Student;
import com.zyy.exception.CustomException;
import com.zyy.service.StudentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author: zhouMiuzi
 * @CreateTime: 2024-12-08
 * @Description: 学生的控制类
 * @Version: 1.0
 */

@RestController
@RequestMapping("/student")
public class StudentController {

    @Resource
    private StudentService studentService;

    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pagenum, @RequestParam(defaultValue = "10") Integer pagesize, Student student) {
        //返回分页数据
        PageInfo<Student> studentPageInfo=studentService.selectPage(pagenum,pagesize,student);
        return Result.success(studentPageInfo);
    }

    @PostMapping("/add")
    public Result add(@RequestBody Student student) {
        try{
            studentService.add(student);
            return Result.success("学生信息：" + student + " 添加成功");
        }catch (IllegalArgumentException e) {
            return Result.error("添加失败: " + e.getMessage());
        }
    }

    @GetMapping("/showStudentInfo")
    public Result showStudentInfo(@RequestParam String username) {
        try {
            Student student = studentService.getStudentByUsername(username);
            return Result.success(student);
        } catch (CustomException e) {
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    @PostMapping("/updatePassword")
    public Result updateStudentPassword(@RequestBody Password request) {
        try {
            studentService.updateStudentPassword(request.getUsername(), request.getOldPassword(), request.getNewPassword());
            return Result.success("学生密码修改成功");
        } catch (IllegalArgumentException e) {
            return Result.error("学生密码修改失败: " + e.getMessage());
        }
    }

    @PostMapping("/updateStudentInfo")
    public Result updateStudentInfo(@RequestBody Student student) {
        try {
            studentService.updateStudentData(student);
            return Result.success(student);
        } catch (CustomException e) {
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{studentId}")
    public Result deleteStudent(@PathVariable int studentId) {
        try {
            studentService.deleteStudent(studentId);
            return Result.success("学生删除成功");
        }catch (IllegalArgumentException e) {
            return Result.error("删除失败: " + e.getMessage());
        }

    }






}
