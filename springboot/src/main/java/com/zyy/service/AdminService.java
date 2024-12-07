package com.zyy.service;

import com.zyy.entity.Admin;
import com.zyy.exception.CustomException;
import com.zyy.mapper.AdminMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: zhouMiuzi
 * @CreateTime: 2024-12-06
 * @Description:
 * @Version: 1.0
 */

@Service
public class AdminService {

    @Resource
    private AdminMapper adminMapper;


    /**
     * 登录
     * */
    public Admin login(Admin admin){
        Admin dbadmin = adminMapper.selectByUserName(admin.getUsername());
        if(dbadmin==null){
            //用户不存在
            throw new CustomException("账号或密码错误");
        }
        if(!admin.getPassword().equals(dbadmin.getPassword())){
            //密码错误
            throw new CustomException("密码错误");
        }
        //登录成功
        return dbadmin;
    }
}
