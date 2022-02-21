package com.cache.service.impl;

import com.cache.dao.UserMapper;
import com.cache.pojo.UserADTO;
import com.cache.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public List<UserADTO> queryUsers() {
        return userMapper.queryUsers();
    }

    @Override
    public UserADTO queryUserById(UserADTO userADTO) {
        return userMapper.queryUserById(userADTO.getId());
    }

    @Override
    public String addUser(UserADTO userADTO) throws Exception {
        int count = userMapper.addUser(userADTO);
        if (count != 1) {
            throw new Exception("插入错误");
        }
        return userADTO.toString() + "插入成功！";
    }
}
