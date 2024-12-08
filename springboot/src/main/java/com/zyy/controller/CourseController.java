package com.zyy.controller;

import com.github.pagehelper.PageInfo;
import com.zyy.common.Result;
import com.zyy.entity.Course;
import com.zyy.service.CourseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: zhouMiuzi
 * @CreateTime: 2024-12-07
 * @Description: 课程控制器
 * @Version: 1.0
 */

@RestController
@RequestMapping("/management/course")
public class CourseController {

    //拿到每一页的数据
    @Resource
    private CourseService courseService;

    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1")Integer pagenum, @RequestParam(defaultValue = "10") Integer pagesize,@RequestParam(defaultValue = "") String search) {
        //需要service去返回分页数据
        PageInfo<Course> coursePageInfo = courseService.selectPage(pagenum, pagesize,search);
        return Result.success(coursePageInfo);
    }

}
