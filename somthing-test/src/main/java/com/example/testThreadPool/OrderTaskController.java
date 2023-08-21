package com.example.testThreadPool;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/orderTaskController")
public class OrderTaskController {
    @Resource
    private OrderTaskService orderTaskService;

    @RequestMapping(path = "/test", method = RequestMethod.GET)
    public void test() throws InterruptedException {
        orderTaskService.orderTask();
    }
}
