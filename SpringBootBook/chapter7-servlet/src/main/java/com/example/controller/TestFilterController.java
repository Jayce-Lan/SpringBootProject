package com.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/filter")
@Slf4j
public class TestFilterController {
    @RequestMapping(value = "testGet", method = RequestMethod.GET)
    public String testGet(String userName) {
        log.info("输入参数：" + userName);
        return userName;
    }

    @RequestMapping(value = "testPost", method = RequestMethod.POST)
    public String testPost(String userName) {
        log.info("输入参数：" + userName);
        return userName;
    }
}
