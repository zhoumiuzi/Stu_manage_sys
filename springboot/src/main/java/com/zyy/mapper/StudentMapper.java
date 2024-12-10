package com.zyy.mapper;

import com.zyy.entity.Student;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Author: zhouMiuzi
 * @CreateTime: 2024-12-08
 * @Description:
 * @Version: 1.0
 */


public interface StudentMapper {

    //根据id选择对象
    @Select("SELECT * FROM stu_manage_sys.students WHERE username=#{username}")
    Student selectByUserName(String username);

    @Select("SELECT * FROM stu_manage_sys.students WHERE studentid=#{studentid}")
    Student findById(Integer studentid);

    //根据名称和学号来查询
    @Select("SELECT * FROM students s WHERE (#{username} IS NULL OR s.username LIKE CONCAT('%', #{username}, '%')) AND (#{name} IS NULL OR s.name LIKE CONCAT('%', #{name}, '%'))")
    List<Student> selectAll(Student student);


    @Insert("INSERT into students(username, name, gender, age, password, phonenum, role) VALUES (#{username}, #{name}, #{gender}, #{age}, #{password}, #{phonenum}, #{role})")
    void insertStudent(Student student);

    @Update("UPDATE students SET password = #{password} WHERE studentid = #{studentid}")
    void updateStudentPassword(Student student);

    @Select("SELECT * FROM students WHERE username = #{username}")
    Student findByStudentname(String username);

}





