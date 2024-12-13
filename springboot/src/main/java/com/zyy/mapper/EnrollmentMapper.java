package com.zyy.mapper;

import com.zyy.entity.Course;
import com.zyy.entity.Enrollment;
import com.zyy.entity.Student;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Author: zhouMiuzi
 * @CreateTime: 2024-12-11
 * @Description:
 * @Version: 1.0
 */

@Mapper
public interface EnrollmentMapper {

    //插入选课记录
    @Insert("INSERT INTO enrollments(studentid, courseid) VALUES (#{studentid}, #{courseid})")
    void insert(Enrollment enrollment);

    //检查重复选课
    @Select("SELECT COUNT(*) > 0 FROM enrollments WHERE studentid = #{studentid} AND courseid = #{courseid}")
    boolean exists(Enrollment enrollment);

    //删除选课记录
    @Delete("DELETE FROM enrollments WHERE studentid = #{studentid} AND courseid = #{courseid}")
    void deleteByStudentAndCourse(Enrollment enrollment);

    //根据id查找已经选了的课程
    @Select("SELECT * FROM enrollments WHERE studentid = #{studentid}")
    List<Enrollment> selectByStudentid(int studentid);

    @Select("SELECT e.enrollmentid, e.courseid, e.studentid,e.grade, e.scoreregisterid, e.registertime, c.coursename, c.coursenum, c.coursetype, " +
            "  IFNULL(t.username) AS teacher_name FROM enrollments e JOIN courses c ON e.courseid = c.courseid LEFT JOIN teachers t ON c.teacherid = t.teacherid WHERE e.studentid = #{studentid} AND (#{enrollment.coursename} IS NULL OR c.coursename LIKE CONCAT('%', #{enrollment.coursename}, '%')) " +
            "ORDER BY e.enrollmentid")
    List<Enrollment> selectPage(@Param("studentid") int studentid, @Param("enrollment") Enrollment enrollment);

    @Select("SELECT * FROM courses WHERE courseid=#{courseid}")
    Course selectByTeacherId(Integer courseid);


    @Select("SELECT DISTINCT c.courseid,c.coursename,c.coursenum,c.coursetype,c.coursecredits,c.coursedesc\n" +
            "FROM courses c INNER JOIN enrollments e ON c.courseid = e.courseid " +
            "WHERE (#{coursename} IS NULL OR c.coursename LIKE CONCAT('%', #{coursename}, '%')) AND (#{coursenum} IS NULL OR c.coursenum LIKE CONCAT('%', #{coursenum}, '%'))")
    List<Course> selectCourses(Course course);

    // 根据课程ID查询选课的学生列表（关联students和enrollments）
    @Select("SELECT e.enrollmentid, e.courseid, e.studentid, e.grade, e.scoreregisterid, e.registertime, s.username as studentname,s.name " +
            "FROM enrollments e " +
            "JOIN students s ON e.studentid = s.studentid " +
            "WHERE e.courseid = #{courseid}")
    List<Enrollment> selectByCourse(int courseid);

    @Update("UPDATE enrollments SET grade=#{grade}, scoreregisterid=#{scoreregisterid}, registertime=#{registertime} WHERE enrollmentid=#{enrollmentid}")
    void updateGrade(Enrollment enrollment);


    // 查询选修课总学分
    @Select("SELECT SUM(c.coursecredits) AS total_elective_credits " +
            "FROM enrollments e JOIN courses c ON e.courseid = c.courseid " +
            "WHERE e.studentid = #{studentid} AND c.coursetype LIKE CONCAT('%',#{coursetype},'%')")
    Integer getTotalElectiveCredits(@Param("studentid") Integer studentid,@Param("coursetype") String coursetype);

    // 查询已完成选修课总学分
    @Select("SELECT SUM(c.coursecredits) AS completed_elective_credits FROM enrollments e " +
            "JOIN courses c ON e.courseid = c.courseid WHERE e.studentid = #{studentid} AND c.coursetype LIKE CONCAT('%',#{coursetype},'%') AND e.grade >= 60")
    Integer getCompletedElectiveCredits(@Param("studentid") Integer studentid,@Param("coursetype") String coursetype);

    // 查询学生选课明细
    @Select("SELECT c.coursename, c.coursenum, c.coursetype, c.coursecredits, t.username AS teachername, e.grade, e.registertime FROM enrollments e JOIN courses c ON e.courseid = c.courseid JOIN teachers t ON c.teacherid = t.teacherid WHERE e.studentid = #{studentid} AND c.coursetype LIKE CONCAT('%',#{coursetype},'%')")
    List<Enrollment> getEnrollmentsByStudentId(@Param("studentid") Integer studentid,@Param("coursetype") String coursetype);

    @Select("SELECT s.username, s.name, " +
            "(SELECT SUM(c.coursecredits) FROM enrollments e JOIN courses c ON e.courseid = c.courseid WHERE e.studentid = s.studentid AND c.coursetype LIKE CONCAT('%',#{coursetype},'%')) AS totalCredits, " +
            "(SELECT SUM(c.coursecredits) FROM enrollments e JOIN courses c ON e.courseid = c.courseid WHERE e.studentid = s.studentid  AND c.coursetype LIKE CONCAT('%',#{coursetype},'%') AND e.grade >= 60) AS completedCredits " +
            "FROM students s WHERE (#{username} IS NULL OR s.username LIKE CONCAT('%',#{username},'%'))")
    List<Student> getStudentCredits(@Param("coursetype") String coursetype,@Param("username")String username);


}
