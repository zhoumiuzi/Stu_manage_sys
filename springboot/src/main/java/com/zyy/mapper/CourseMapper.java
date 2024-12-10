package com.zyy.mapper;

import com.zyy.entity.Course;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Author: zhouMiuzi
 * @CreateTime: 2024-12-07
 * @Description:
 * @Version: 1.0
 */

public interface CourseMapper {

    //根据名字选id
    @Select("SELECT teacherid FROM teachers WHERE username = #{username}")
    Integer fineteacherID(String username);


    //根据id选择对象
    @Select("SELECT * FROM courses WHERE courseid=#{courseid}")
    Course selectCourseById(Integer courseid);

    //更新
    @Update("UPDATE courses SET coursename = #{coursename}, coursenum = #{coursenum}, " +
            "coursetype = #{coursetype}, coursecredits = #{coursecredits}, teacherid = #{teacherid}, " +
            "coursedesc = #{coursedesc} WHERE courseid = #{courseid}")
    void updateCourse(Course course);

    //插入新数据
    @Insert("INSERT into courses(coursename, coursenum, coursetype, coursecredits, teacherid, coursedesc) VALUES(#{coursename},#{coursenum},#{coursetype},#{coursecredits},#{teacherid},#{coursedesc}) ")
    void insert(Course course);

    @Select("SELECT c.courseId, c.coursename, c.coursenum, c.courseType, c.coursecredits, c.coursedesc, " +
            "t.teacherid, IFNULL(t.username, '暂无教师') AS username " +
            "FROM courses c " +
            "LEFT JOIN stu_manage_sys.teachers t ON c.teacherid = t.teacherid " +
            "WHERE (#{coursename} IS NULL OR c.coursename LIKE CONCAT('%', #{coursename}, '%')) " +
            "AND (#{coursenum} IS NULL OR c.coursenum LIKE CONCAT('%', #{coursenum}, '%')) " +
            "AND (#{username} IS NULL OR #{username} = '' OR t.username LIKE CONCAT('%', #{username}, '%')) " +
            "ORDER BY c.courseid DESC")
    List<Course> selectAll(Course course);

    @Delete("DELETE FROM courses WHERE courseid= #{courseid}")
    void deleteById(Integer courseid);


    @Select("SELECT COUNT(*) FROM courses WHERE coursenum = #{coursenum}")
    Integer findByCoursenum(Integer coursenum);

    @Select("SELECT courseid FROM courses WHERE coursenum = #{coursenum}")
    Integer findCourseIdByCoursenum(Integer coursenum);
}
