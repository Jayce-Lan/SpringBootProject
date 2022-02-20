package com.example.controller;

import com.example.dao.adb.AdbUserMapper;
import com.example.dao.drds.DrdsUserMapper;
import com.example.po.UserA;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private AdbUserMapper adbUserMapper;
    @Resource
    private DrdsUserMapper drdsUserMapper;

    @RequestMapping("/queryAdbUsers")
    public List<UserA> queryAdbUsers() {
        return adbUserMapper.queryUsers();
    }

    @RequestMapping("/queryDrdsUsers")
    public List<UserA> queryDrdsUsers() {
        return drdsUserMapper.queryUsers();
    }
}
