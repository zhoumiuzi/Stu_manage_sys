package com.zyy.mapper;

import cn.hutool.json.JSONUtil;
import com.zyy.entity.Course;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author: zhouMiuzi
 * @CreateTime: 2024-12-07
 * @Description:
 * @Version: 1.0
 */

public interface CourseMapper {

    @Select("SELECT c.courseId, c.courseName, c.coursenum, c.courseType,c.coursecredits, c.coursedesc,t.teacherid, t.username from courses c left join stu_manage_sys.teachers t on c.teacherid = t.teacherid order by c.courseid desc ")
    List<Course> selectAll();


}
