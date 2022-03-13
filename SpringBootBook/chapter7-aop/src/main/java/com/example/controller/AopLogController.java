package com.example.controller;

import com.example.dto.UserDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AopLogController {
    @PostMapping("aoptest1")
    public String aopTest1() {
        return "Hello, AOP test";
    }

    @PostMapping("/aoptest2")
    public UserDTO aopTest2(UserDTO userDTO) {
        return userDTO;
    }

    @PostMapping("/aoptestex")
    public UserDTO aopTestEx(UserDTO userDTO) {
        int i = 1 / 0;
        return userDTO;
    }
}
