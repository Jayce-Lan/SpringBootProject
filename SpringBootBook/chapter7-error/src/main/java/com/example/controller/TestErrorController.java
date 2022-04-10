package com.example.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
/*
@RequestMapping("${server.error.path:${error:path:/error}}")
 * Spring 提供了默认的错误映射地址：/error，因此也可以写成下面的方式
 */
@RequestMapping("/error")
// 继承Spring Boot提供的ErrorController类
public class TestErrorController implements ErrorController {
    public String getErrorPath() {
        return null;
    }

    /**
     * 需要添加URL映射，指向error
     * @return
     */
    @RequestMapping
    public Map<String, Object> handleError() {
        // 用 Map 容器返回信息
        Map<String, Object> map = new HashMap<>();
        map.put("code", 404);
        map.put("msg", "Not Found");
        return map;
    }

    /**
     * 根据请求返回相应的数据格式
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @RequestMapping(value = "", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String errorHtml404(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return "404 Not Found!";
    }

    /**
     * 一个返回正常的方法
     * @return
     */
    @RequestMapping("/ok")
    public Map<String, Object> noError() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("msg", "Success");
        return map;
    }
}
