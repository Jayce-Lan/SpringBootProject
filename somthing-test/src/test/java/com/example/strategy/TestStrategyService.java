package com.example.strategy;

import com.example.strategy2.StrategyConfig;
import com.example.strategy2.StrategyService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
public class TestStrategyService {
    @Resource
    private StrategyConfig strategyConfig;

    @Test
    void testCall() {
        StrategyService strategyService = strategyConfig.getStrategyService("1");
        strategyService.doSome();
    }
}
