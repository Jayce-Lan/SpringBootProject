package com.example.controller;

import com.example.mapper.UserMapper;
import com.example.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin // 解决跨域问题
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/queryUserList")
    public List<User> queryUserList() {
        return userMapper.queryUserList();
    }

    @PostMapping("/addUser")
    public String addUser(User user) throws Exception {
        int count = userMapper.addUser(user);
        if (count != 1) {
            throw new Exception("错误");
        }
        return "ok";
    }

    @GetMapping("/addUser2")
    public String addUser2() throws Exception {
        User user = new User();
        user.setName("李四");
        user.setPwd("qwert");
        int count = userMapper.addUser(user);
        if (count != 1) {
            throw new Exception("错误！");
        }
        return "ok";
    }

    @PostMapping("/updateUser")
    public String updateUser(User user) throws Exception {
        int count = userMapper.updateUser(user);
        if (count != 1) {
            throw new Exception("错误");
        }
        return "ok";
    }

    @PostMapping("/deleteUser")
    public String deleteUser(User user) throws Exception {
        int id = user.getId();
        int count = userMapper.deleteUserById(id);
        if (count != 1) {
            throw new Exception("错误");
        }
        return "ok";
    }
}
