package com.example.strategy;

import com.example.strategy1.StrategyToNoBean;
import com.example.strategy1.impl.StrategyToNoBean1Impl;
import com.example.strategy1.impl.StrategyToNoBean2Impl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 简单策略模式的测试类
 */
@SpringBootTest
@Slf4j
public class TestStrategy1 {
    private static final List<StrategyToNoBean> STRATEGIES = Arrays.asList(new StrategyToNoBean1Impl(), new StrategyToNoBean2Impl());
    private static final Map<String, StrategyToNoBean> STRATEGY_MAP;

    static {
        STRATEGY_MAP = STRATEGIES.stream().collect(Collectors.toMap(StrategyToNoBean :: flag, s -> s));
    }

    @Test
    void test1() {
        STRATEGY_MAP.get("2").process();
    }
}
