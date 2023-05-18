package com.example.strategy1.impl;

import com.example.strategy1.StrategyToNoBean;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StrategyToNoBean1Impl implements StrategyToNoBean {
    @Override
    public String flag() {
        return "1";
    }

    @Override
    public void process() {
        log.info("StrategyToNoBean1Impl >>>>>> 1");
    }
}
