package com.example.strategy2.impl;

import com.example.strategy2.StrategyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("strategyService1Impl")
@Slf4j
public class StrategyService1Impl implements StrategyService {
    @Override
    public String serviceFlag() {
        return "1";
    }

    @Override
    public void doSome() {
        log.info("StrategyService1Impl >>>>>> 1");
    }
}
