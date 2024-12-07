package com.zyy.mapper;

import com.zyy.entity.Admin;
import org.apache.ibatis.annotations.Select;

/**
 * @Author: zhouMiuzi
 * @CreateTime: 2024-12-06
 * @Description:
 * @Version: 1.0
 */

public interface AdminMapper {

    //写的是静态查询，所以直接在mapper里面写就可以了提供查询方法
    @Select("select * from managers where username = #{username}")
    Admin selectByUserName(String username);

}
