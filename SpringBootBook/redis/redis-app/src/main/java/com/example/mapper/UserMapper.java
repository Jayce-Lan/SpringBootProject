package com.example.mapper;

import com.example.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    /**
     * 根据手机号查询用户
     * @param phone
     * @return
     */
    User queryUserInfo(String phone);

    /**
     * 创建新的用户
     * @param user
     * @return
     */
    int addUser(User user);
}
