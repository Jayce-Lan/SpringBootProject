package com.example;

import com.example.direct.ProducerDirect;
import com.example.util.RabbitmqConst;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * 路由模式测试类
 */
@SpringBootTest
@Slf4j
public class DirectTests {
    @Resource
    private ProducerDirect producerDirect;

    @Test
    void testProducerDirect() {
        producerDirect.produce("direct.Q4", "Test Q1");
        producerDirect.produce(RabbitmqConst.DIRECT_Q2, "Test Q3");
    }
}
