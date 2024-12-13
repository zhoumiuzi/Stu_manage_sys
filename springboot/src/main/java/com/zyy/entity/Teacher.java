package com.zyy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zhouMiuzi
 * @CreateTime: 2024-12-11
 * @Description:
 * @Version: 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Teacher {
    private int teacherid;
    private String username;
    private String title;
    private float salary;

}
