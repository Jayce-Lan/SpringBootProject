package com.example.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/servletDemo")
public class TestServletController {

    /**
     * 由于被Servlet处理了请求，因此，返回值会被重写
     * @return
     */
    @RequestMapping("/test")
    public String test() {
        return "Servlet";
    }
}
