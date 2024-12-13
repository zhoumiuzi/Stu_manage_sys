package com.zyy.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zyy.common.Result;
import com.zyy.entity.Course;
import com.zyy.entity.Enrollment;
import com.zyy.entity.Student;
import com.zyy.mapper.EnrollmentMapper;
import org.hibernate.annotations.Source;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhouMiuzi
 * @CreateTime: 2024-12-11
 * @Description:
 * @Version: 1.0
 */

@Service
public class EnrollmentService {

    @Resource
    EnrollmentMapper enrollmentMapper;

    public void add(Enrollment enrollment) {
        Course course = enrollmentMapper.selectByTeacherId(enrollment.getCourseid());

        // 检查是否重复选课
        if (enrollmentMapper.exists(enrollment)) {
            throw new IllegalArgumentException("重复选课，选课记录已存在");
        }

        if (course.getTeacherid() == null) {
            throw new IllegalArgumentException("没有老师的课程不能选！");
        }

        enrollmentMapper.insert(enrollment);
    }

    public void delete(Enrollment enrollment) {
        enrollmentMapper.deleteByStudentAndCourse(enrollment);
    }

    public List<Enrollment> listByStudent(int studentid) {
        return enrollmentMapper.selectByStudentid(studentid);
    }


    public PageInfo<Enrollment> selectPage(Integer pagenum, Integer pagesize, Integer studentid, Enrollment enrollment) {
        PageHelper.startPage(pagenum, pagesize);
        List<Enrollment> list = enrollmentMapper.selectPage(studentid, enrollment);
        return PageInfo.of(list);
    }

    public PageInfo<Course> selectCoursesPage(int pagenum, int pagesize,Course course) {
        PageHelper.startPage(pagenum, pagesize);
        List<Course> list = enrollmentMapper.selectCourses(course);
        return PageInfo.of(list);
    }

    public List<Enrollment> listByCourse(int courseid) {
        return enrollmentMapper.selectByCourse(courseid);
    }

    //成绩更新
    public void updateGrade(Integer enrollmentid, Float grade, Integer scoreregisterid) {
        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentid(enrollmentid);
        enrollment.setGrade(grade);
        enrollment.setScoreregisterid(scoreregisterid);
        enrollment.setRegistertime(LocalDateTime.now());
        enrollmentMapper.updateGrade(enrollment);
    }

    //这个是看学生端的自己的成绩的，所以需要学生id
    public Map<String, Object> getEnrollmentInfo(Integer studentid,String coursetype) {
        Map<String, Object> result = new HashMap<>();
        Integer totalElective = enrollmentMapper.getTotalElectiveCredits(studentid,coursetype);
        Integer completedElective = enrollmentMapper.getCompletedElectiveCredits(studentid,coursetype);
        List<Enrollment> enrollments = enrollmentMapper.getEnrollmentsByStudentId(studentid,coursetype);

        result.put("totalElectiveCredits", totalElective == null ? 0 : totalElective);
        result.put("completedElectiveCredits", completedElective == null ? 0 : completedElective);
        result.put("enrollments", enrollments);
        return result;
    }

    //查看所有的学生的学分
    public  PageInfo<Student> getStudentCredits(Integer pageNum, Integer pageSize,String coursetype,String username) {
        PageHelper.startPage(pageNum, pageSize);
        List<Student> studentlist = enrollmentMapper.getStudentCredits(coursetype,username);
        // System.out.println(courseList);
        return PageInfo.of(studentlist);
    }



}
