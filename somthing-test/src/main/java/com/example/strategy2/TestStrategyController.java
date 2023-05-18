package com.example.strategy2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/strategy2")
public class TestStrategyController {
    @Resource
    private StrategyConfig strategyConfig;

    @GetMapping("/testCall/{flag}")
    public String testCall(@PathVariable("flag") String flag) {
        strategyConfig.getStrategyService(flag).doSome();
        return "success!";
    }
}
