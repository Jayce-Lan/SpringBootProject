package com.example.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

/**
 * 测试按需引入配置文件
 */
@SpringBootTest
@Slf4j
public class TestConfigApplication {
    @Value("${my.configtest.name}")
    private String name;
    @Value("${my.configtest.value}")
    private BigDecimal value;
    @Value("${my.configtest.rm}")
    private String rm;
    @Value("${my.configtest.mq}")
    private String mq;

    @Test
    void testRead() {
        log.info("name: {}", this.name);
        log.info("value: {}", this.value);
        log.info("rm: {}", this.rm);
        log.info("mq: {}", this.mq);
    }
}
