package com.zyy.entity;



import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import lombok.Data;
/**
 * @Author: zhouMiuzi
 * @CreateTime: 2024-12-06
 * @Description: 管理员类
 * @Version: 1.0
 */

public class Admin {


    private Integer adminId;


    private String name;


    private String username;

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
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