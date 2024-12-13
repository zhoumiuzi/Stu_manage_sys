package com.zyy.mapper;

import com.zyy.entity.Teacher;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Author: zhouMiuzi
 * @CreateTime: 2024-12-11
 * @Description:
 * @Version: 1.0
 */

@Mapper
public interface TeacherMapper {

    @Select("SELECT * FROM teachers WHERE username = #{username}")
    Teacher selectByUsername(String username);

    @Select("SELECT * FROM teachers")
    List<Teacher> selectAll(Teacher teacher);

    @Insert("INSERT INTO teachers (username, title, salary) VALUES (#{username}, #{title}, #{salary})")
    void insertTeacher(Teacher teacher);

    @Update("UPDATE teachers SET username = #{username}, title = #{title}, salary = #{salary} WHERE teacherid = #{teacherid}")
    void updateTeacher(Teacher teacher);

    @Delete("DELETE FROM teachers WHERE teacherid = #{teacherid}")
    void deleteByTeacherId(int teacherId);
}
