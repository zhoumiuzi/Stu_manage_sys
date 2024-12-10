package com.zyy.controller;

import com.github.pagehelper.PageInfo;
import com.zyy.common.Result;
import com.zyy.entity.Student;
import com.zyy.service.StudentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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


}
