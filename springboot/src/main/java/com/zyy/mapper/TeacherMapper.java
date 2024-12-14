package com.zyy.mapper;

import com.zyy.entity.Teacher;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

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

    // 统计不同职称的教师数量和平均工资
    @Select("SELECT title, COUNT(*) AS count, AVG(salary) AS avgSalary FROM teachers GROUP BY title")
    List<Map<String, Object>> countAndAvgSalaryByTitle();

    // 统计每个教师所教每门课程的平均成绩、最高分、最低分
    @Select("SELECT t.teacherid, t.username, c.courseid, c.coursename, " +
            "AVG(e.grade) AS avgGrade, MAX(e.grade) AS maxGrade, MIN(e.grade) AS minGrade " +
            "FROM teachers t " +
            "JOIN courses c ON t.teacherid = c.teacherid " +
            "JOIN enrollments e ON c.courseid = e.courseid " +
            "GROUP BY t.teacherid, c.courseid")
    List<Map<String, Object>> getCourseStatisticsByTeacher();

}
