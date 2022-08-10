package com.example.controller;

import com.example.dto.LoginFormDTO;
import com.example.dto.Result;
import com.example.service.UserService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("user")
public class UserController {
    @Resource
    UserService userService;

    @RequestMapping("code")
    public Result code(String phone) {
        return userService.sendCode(phone);
    }

    /**
     * 校验登陆，短信验证码、密码和帐号
     * @param loginFormDTO 由于是以json形式传过来的，因此需要使用@RequestBody注解
     * @return
     */
    @RequestMapping("login")
    public Result login(@RequestBody LoginFormDTO loginFormDTO) {
//        return Result.ok(loginFormDTO);
        return userService.login(loginFormDTO);
    }
}
