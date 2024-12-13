package com.zyy.entity;

import lombok.Data;

/**
 * @Author: zhouMiuzi
 * @CreateTime: 2024-12-10
 * @Description:
 * @Version: 1.0
 */

@Data
public class Password {
    private String username;
    private String oldPassword;
    private String newPassword;
}
