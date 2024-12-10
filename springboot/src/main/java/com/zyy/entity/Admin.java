package com.zyy.entity;


/**
 * @Author: zhouMiuzi
 * @CreateTime: 2024-12-06
 * @Description: 管理员类
 * @Version: 1.0
 */

public class Admin extends Account{


    private Integer adminid;


    private String name;


    private String username;

    public Integer getAdminid() {
        return adminid;
    }

    public void setAdminid(Integer adminid) {
        this.adminid = adminid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password; // 和学生一样，密码需要加密存储，要公！平！


}