package com.zyy.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zyy.entity.Teacher;
import com.zyy.exception.CustomException;
import com.zyy.mapper.TeacherMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: zhouMiuzi
 * @CreateTime: 2024-12-11
 * @Description:
 * @Version: 1.0
 */

@Service
public class TeacherService {

    @Resource
    private TeacherMapper teacherMapper;

    public PageInfo<Teacher> selectPage(Integer pagenum, Integer pagesize, Teacher teacher) {
        PageHelper.startPage(pagenum, pagesize);
        List<Teacher> teachers = teacherMapper.selectAll(teacher);
        return PageInfo.of(teachers);
    }

    public void addTeacher(Teacher teacher) {
        Teacher existingTeacher = teacherMapper.selectByUsername(teacher.getUsername());
        if (existingTeacher != null) {
            throw new CustomException("教师用户名已存在");
        }
        teacherMapper.insertTeacher(teacher);
    }

    public void updateTeacher(Teacher teacher) {
        teacherMapper.updateTeacher(teacher);
    }

    public void deleteTeacher(int teacherId) {
        teacherMapper.deleteByTeacherId(teacherId);
    }
}
