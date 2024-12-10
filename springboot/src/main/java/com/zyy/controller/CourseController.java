package com.zyy.controller;

import com.github.pagehelper.PageInfo;
import com.zyy.common.Result;
import com.zyy.entity.Course;
import com.zyy.service.CourseService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

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
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pagenum, @RequestParam(defaultValue = "10") Integer pagesize, Course course) {
        //需要service去返回分页数据
        PageInfo<Course> coursePageInfo = courseService.selectPage(pagenum, pagesize, course);
        return Result.success(coursePageInfo);
    }

    @PostMapping("/add")
    //RRequestBody把前端的json数据传入
    public Result add(@RequestBody Course course) {
        try {
            // 检查课程编号是否重复
            if (courseService.isCoursenumExists(course.getCoursenum())) {
                return Result.error("课程编号已存在，请重新输入");
            }
            courseService.addData(course);
            return Result.success("课程信息：" + course + " 添加成功");
        } catch (IllegalArgumentException e) {
            return Result.error("添加失败: " + e.getMessage());
        }catch (DuplicateKeyException e) { // 捕获数据库唯一约束异常
            return Result.error("课程号已存在，请输入唯一的学号");
        }
    }

    @PutMapping("/update")
    public Result update(@RequestBody Course course) {
        try {
            courseService.updateData(course);
            return Result.success("课程信息修改成功");
        } catch (IllegalArgumentException e) {
            return Result.error("修改失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{courseid}")
    public Result delete(@PathVariable Integer courseid) {
        try {
            courseService.deleteDataById(courseid);
            return Result.success("课程信息删除成功");
        } catch (IllegalArgumentException e) {
            return Result.error("删除失败: " + e.getMessage());
        }
    }

    @GetMapping("/checkCoursenum")
    public Result checkCoursenum(@RequestParam int coursenum) {
        boolean exists = courseService.isCoursenumExists(coursenum);
        return Result.success(Map.of("exists", exists));
    }

}
