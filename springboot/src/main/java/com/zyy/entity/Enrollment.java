package com.zyy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author: zhouMiuzi
 * @CreateTime: 2024-12-11
 * @Description: 记录学生选课，登记分数
 * @Version: 1.0
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Enrollment {
    private Integer enrollmentid;
    private Integer courseid;
    private Integer scoreregisterid;
    private Integer studentid;
    private float grade;
    private LocalDateTime registertime;
    private String coursename;
    private String studentname;
    private String name;
    private Integer coursenum;
    private Integer coursecredits;
    private String coursetype;
    private String teachername;
}
