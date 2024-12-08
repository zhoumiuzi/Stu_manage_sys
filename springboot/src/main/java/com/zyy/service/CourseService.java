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
        // 同样的逻辑应用在更新上
        if (course.getUsername() == null || course.getUsername().trim().isEmpty()
                || "暂无教师".equals(course.getUsername().trim())) {
            course.setTeacherid(null);
        } else {
            Integer teacherid = courseMapper.fineteacherID(course.getUsername());
            course.setTeacherid(teacherid);
        }
        courseMapper.updateCourse(course);
    }


    public void deleteDataById(Integer id) {
        courseMapper.deleteById(id);
    }

    //返回两个值，一个是查询的总数，还有一个是数据列表
    //pagenum是当前页码，pagesize是每一页的个数
    public PageInfo<Course> selectPage(Integer pageNum, Integer pageSize, Course course) {
        PageHelper.startPage(pageNum, pageSize);
        List<Course> courseList = courseMapper.selectAll(course);
        // System.out.println(courseList);
        return PageInfo.of(courseList);
    }


}
