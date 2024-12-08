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

    //返回两个值，一个是查询的总数，还有一个是数据列表
    //pagenum是当前页码，pagesize是每一页的个数
    public PageInfo<Course> selectPage(Integer pageNum, Integer pageSize,String search) {
        PageHelper.startPage(pageNum, pageSize);
        List<Course> courseList = courseMapper.selectAll(search);
        System.out.println(courseList);
        return PageInfo.of(courseList);
    }

}
