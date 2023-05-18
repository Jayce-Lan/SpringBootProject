package com.example.strategy1.impl;

import com.example.strategy1.StrategyToNoBean;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StrategyToNoBean2Impl implements StrategyToNoBean {
    @Override
    public String flag() {
        return "2";
    }

    @Override
    public void process() {
        log.info("StrategyToNoBean2Impl >>>>>> 2");
    }
}
