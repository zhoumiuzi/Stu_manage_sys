package com.zyy.mapper;

import com.zyy.entity.Admin;
import com.zyy.entity.Student;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Author: zhouMiuzi
 * @CreateTime: 2024-12-06
 * @Description:
 * @Version: 1.0
 */

public interface AdminMapper {

    //写的是静态查询，所以直接在mapper里面写就可以了提供查询方法
    @Select("SELECT * FROM stu_manage_sys.managers WHERE username=#{username}")
    Admin selectByUserName(String username);

    @Update("UPDATE managers SET name = #{name},password=#{password}")
    void updateAdmin(Admin admin);

    @Select("SELECT * FROM students WHERE (#{username} IS NULL OR username LIKE CONCAT('%',#{username},'%'))AND (#{name} IS NULL OR username LIKE CONCAT('%',#{name},'%'))")
    List<Student> selectAll(Student student);
}
