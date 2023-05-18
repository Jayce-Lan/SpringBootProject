package com.example.strategy2.impl;

import com.example.strategy2.StrategyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("strategyService2Impl")
@Slf4j
public class StrategyService2Impl implements StrategyService {
    @Override
    public String serviceFlag() {
        return "2";
    }

    @Override
    public void doSome() {
        log.info("StrategyService2Impl >>>>>> 2");
    }
}
