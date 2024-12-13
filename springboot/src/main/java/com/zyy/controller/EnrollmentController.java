package com.zyy.controller;

import com.github.pagehelper.PageInfo;
import com.zyy.common.Result;
import com.zyy.entity.Course;
import com.zyy.entity.Enrollment;
import com.zyy.entity.Student;
import com.zyy.service.EnrollmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: zhouMiuzi
 * @CreateTime: 2024-12-11
 * @Description:
 * @Version: 1.0
 */

@RestController
@RequestMapping("/courseselect")
public class EnrollmentController {

    @Resource
    EnrollmentService enrollmentService;

    @PostMapping("/addcourse")
    public Result addcourse(@RequestBody Enrollment enrollment) {
        try{
            enrollmentService.add(enrollment);
            return Result.success();
        }catch (IllegalArgumentException e){
            return Result.error(e.getMessage());
        }

    }

    @DeleteMapping("/deletecourse")
    public Result deletecourse(@RequestBody Enrollment enrollment) {
        enrollmentService.delete(enrollment);
        return Result.success();
    }

    @GetMapping("/listByStudent")
    public Result listByStudent(@RequestParam int studentid) {
        // 返回该学生已选课程列表，比如[{courseid:1},{courseid:2},...]
        List<Enrollment> list = enrollmentService.listByStudent(studentid);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pagenum,
                             @RequestParam(defaultValue = "18") Integer pagesize,
                             @RequestParam Integer studentid,Enrollment enrollment) {
        PageInfo<Enrollment> pageInfo = enrollmentService.selectPage(pagenum, pagesize, studentid, enrollment);
        return Result.success(pageInfo);
    }


    // 管理员查看课程列表(分页查询)
    @GetMapping("/CourseList")
    public Result adminCourseList(@RequestParam(defaultValue = "1") Integer pagenum,
                                  @RequestParam(defaultValue = "18") Integer pagesize,
                                  Course course) {
        PageInfo<Course> pageInfo = enrollmentService.selectCoursesPage(pagenum, pagesize,course);
        return Result.success(pageInfo);
    }

    // 根据课程ID查询该课程下的学生列表
    @GetMapping("/courseStudents")
    public Result courseStudents(@RequestParam Integer courseid) {
        List<Enrollment> list = enrollmentService.listByCourse(courseid);
        return Result.success(list);
    }

    // 成绩录入
    @PostMapping("/updateGrade")
    public Result updateGrade(@RequestParam Integer enrollmentid,
                              @RequestParam Float grade,
                              @RequestParam Integer scoreregisterid) {
        enrollmentService.updateGrade(enrollmentid, grade, scoreregisterid);
        return Result.success();
    }

    @GetMapping("/StudentCourseList")
    public ResponseEntity<?> getCourseList(@RequestParam Integer studentid,
                                           @RequestParam(required = false) String coursename,
                                           @RequestParam(required = false) String coursenum,
                                           @RequestParam(defaultValue = "1") Integer pagenum,
                                           @RequestParam(defaultValue = "18") Integer pagesize,
                                           @RequestParam(required = false) String coursetype) {
        // 此处coursename、coursenum用于前端输入查询过滤
        Map<String, Object> data = enrollmentService.getEnrollmentInfo(studentid,coursetype);

        // 简单过滤（非SQL层动态查询，仅Java内存中过滤）
        List<Enrollment> allEnrollments = (List<Enrollment>) data.get("enrollments");

        if (coursename != null && !coursename.trim().isEmpty()) {
            allEnrollments = allEnrollments.stream()
                    .filter(e -> e.getCoursename() != null && e.getCoursename().contains(coursename))
                    .collect(Collectors.toList());
        }
        if (coursenum != null && !coursenum.trim().isEmpty()) {
            try {
                int num = Integer.parseInt(coursenum);
                allEnrollments = allEnrollments.stream()
                        .filter(e -> e.getCoursenum() != null && e.getCoursenum().equals(num))
                        .collect(Collectors.toList());
            } catch (NumberFormatException ex) {
                // 无效输入则不过滤
            }
        }

        int total = allEnrollments.size();
        int fromIndex = Math.max((pagenum - 1) * pagesize, 0);
        int toIndex = Math.min(fromIndex + pagesize, total);
        List<Enrollment> pageList = fromIndex < toIndex ? allEnrollments.subList(fromIndex, toIndex) : Collections.emptyList();

        Map<String, Object> result = new HashMap<>();
        result.put("list", pageList);
        result.put("total", total);
        result.put("totalElectiveCredits", data.get("totalElectiveCredits"));
        result.put("completedElectiveCredits", data.get("completedElectiveCredits"));

        return ResponseEntity.ok(result);
    }


    // 管理员获取学生的总学分和已完成学分
    @GetMapping("/studentCredits")
    public Result getStudentCredits(@RequestParam(defaultValue = "1") Integer pagenum, @RequestParam(defaultValue = "18") Integer pagesize,@RequestParam String coursetype,@RequestParam String username) {
        // 查询所有学生的学分数据
        PageInfo<Student> studentPageInfo = enrollmentService.getStudentCredits(pagenum, pagesize,coursetype,username);
        return Result.success(studentPageInfo);
    }

}
