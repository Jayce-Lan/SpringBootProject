package com.example.strategy2.impl;

import com.example.strategy2.StrategyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("strategyService3Impl")
@Slf4j
public class StrategyService3Impl implements StrategyService {
    @Override
    public String serviceFlag() {
        return "3";
    }

    @Override
    public void doSome() {
        log.info("StrategyService3Impl >>>>>> 3");
    }
}
