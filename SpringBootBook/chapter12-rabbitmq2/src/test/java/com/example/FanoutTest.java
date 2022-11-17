package com.example;

import com.example.fanout.ProducerFanout;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
public class FanoutTest {
    @Resource
    private ProducerFanout producerFanout;

    @Test
    void testFanout() {
        producerFanout.produce("广播模式下发送消息");
    }
}
