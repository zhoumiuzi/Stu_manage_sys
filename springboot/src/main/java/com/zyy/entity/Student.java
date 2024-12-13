package com.zyy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author: zhouMiuzi
 * @CreateTime: 2024-12-08
 * @Description: 学生实体类
 * @Version: 1.0
 */

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student extends Account{

    private int studentid;
    private String username;
    private String name;
    private String password;
    private int age;
    private String gender;
    private String phonenum;
    private float totalCredits;    // 总学分
    private float completedCredits; // 已完成学分
}
