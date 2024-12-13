package com.zyy.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zyy.entity.Course;
import com.zyy.mapper.CourseMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: zhouMiuzi
 * @CreateTime: 2024-12-07
 * @Description:
 * @Version: 1.0
 */

@Service
public class CourseService {

    @Resource
    private CourseMapper courseMapper;

    //新增加传入数据,因为前端输入的是老师的名称，所以需要获取老师的id再填入
    public void addData(Course course) {
        if (courseMapper.findByCoursenum(course.getCoursenum()) >0) {
            throw new IllegalArgumentException("课程号已存在");
        }
        // 如果username为null、空字符串、或“暂无教师”，则teacherid=null
        if (course.getUsername() == null || course.getUsername().trim().isEmpty()
                || "暂无教师".equals(course.getUsername().trim())) {
            course.setTeacherid(null);
        } else {
            Integer teacherid = courseMapper.fineteacherID(course.getUsername());
            if (teacherid == null) {
                // 找不到对应教师则设置为null
                course.setTeacherid(null);
            } else {
                course.setTeacherid(teacherid);
            }
        }
        courseMapper.insert(course);
    }

    public void updateData(Course course) {
        if (course.getCourseid() == null) {
            throw new IllegalArgumentException("课程ID不能为空");
        }

        // 检查更新时课程编号是否已存在（排除当前课程的编号）
        Integer existingId = courseMapper.findCourseIdByCoursenum(course.getCoursenum());
        if (existingId != null && !existingId.equals(course.getCourseid())) {
            throw new IllegalArgumentException("课程编号重复！请重新设置课程编号！");
        }

        // 设置教师ID
        if (course.getUsername() == null || course.getUsername().trim().isEmpty() || "暂无教师".equals(course.getUsername().trim())) {
            course.setTeacherid(null);
        } else {
            Integer teacherid = courseMapper.fineteacherID(course.getUsername());
            course.setTeacherid(teacherid);
        }

        // 更新课程数据
        courseMapper.updateCourse(course);
    }


    public boolean isCoursenumExists(Integer coursenum) {
        return courseMapper.findByCoursenum(coursenum) > 0;
    }


    public void deleteDataById(Integer id) {
        courseMapper.deleteById(id);
    }

    //返回两个值，一个是查询的总数，还有一个是数据列表
    //pagenum是当前页码，pagesize是每一页的个数
    public PageInfo<Course> selectPage(Integer pageNum, Integer pageSize, Course course) {
        PageHelper.startPage(pageNum, pageSize);
        List<Course> courseList = courseMapper.selectAll(course);
        return PageInfo.of(courseList);
    }


}
