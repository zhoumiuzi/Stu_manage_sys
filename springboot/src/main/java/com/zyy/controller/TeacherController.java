package com.zyy.controller;

import com.github.pagehelper.PageInfo;
import com.zyy.common.Result;
import com.zyy.entity.Teacher;
import com.zyy.exception.CustomException;
import com.zyy.service.TeacherService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author: zhouMiuzi
 * @CreateTime: 2024-12-11
 * @Description:
 * @Version: 1.0
 */

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Resource
    private TeacherService teacherService;

    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pagenum,
                             @RequestParam(defaultValue = "10") Integer pagesize,
                             Teacher teacher) {
        PageInfo<Teacher> teacherPageInfo = teacherService.selectPage(pagenum, pagesize, teacher);
        return Result.success(teacherPageInfo);
    }

    @PostMapping("/add")
    public Result add(@RequestBody Teacher teacher) {
        try {
            teacherService.addTeacher(teacher);
            return Result.success("教师添加成功");
        } catch (CustomException e) {
            return Result.error("添加失败：" + e.getMessage());
        }
    }

    @PutMapping("/update")
    public Result update(@RequestBody Teacher teacher) {
        try {
            teacherService.updateTeacher(teacher);
            return Result.success("教师信息更新成功");
        } catch (CustomException e) {
            return Result.error("更新失败：" + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{teacherId}")
    public Result delete(@PathVariable int teacherId) {
        try {
            teacherService.deleteTeacher(teacherId);
            return Result.success("教师删除成功");
        } catch (CustomException e) {
            return Result.error("删除失败：" + e.getMessage());
        }
    }

}
