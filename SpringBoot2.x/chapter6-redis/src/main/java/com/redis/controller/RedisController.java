package com.redis.controller;

import com.redis.pojo.RedisUser;
import com.redis.service.RedisService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/redis")
public class RedisController {
    @Resource
    private RedisService redisService;

    @RequestMapping("/addUser")
    public String addUser(RedisUser redisUser) {
        redisService.set(redisUser.getId().toString(), redisUser);
        return "success";
    }

    @RequestMapping("/queryUserById")
    public Object queryUserById(RedisUser user) {
        return redisService.get(user.getId().toString());
    }
}
