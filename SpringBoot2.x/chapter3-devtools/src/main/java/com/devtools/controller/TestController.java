package com.devtools.controller;

import com.devtools.config.MorePropertiesBean;
import com.devtools.config.TestConfigBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Value("${test.name}")
    private String name;

    @Value("${test.author}")
    private String author;

    @Autowired
    private MorePropertiesBean morePropertiesBean;

    @Autowired
    private TestConfigBean testConfigBean;

    @GetMapping("test1")
    public String test1() {
        return "【" + name + "】【" + author + "】";
    }

    @GetMapping("test2")
    public String test2() {
        return testConfigBean.toString();
    }

    @GetMapping("test3")
    public String testMorePropertiesBean() {
        return morePropertiesBean.getName();
    }
}
