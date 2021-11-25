package com.springmvc.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("indexcontroller")
public class IndexController {
    @RequestMapping("/test")
    public Map<String, String> testMap() {
        Map<String, String> map = new HashMap<>();
        map.put("key", "value");
        return map;
    }

    @RequestMapping("/test2")
    public String add() {
        return "I just add .";
    }
}
