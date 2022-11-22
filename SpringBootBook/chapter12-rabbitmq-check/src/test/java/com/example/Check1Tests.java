package com.example;

import com.example.check1.ProducerCheck1;
import com.example.utils.RabbitMQConst;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
public class Check1Tests {
    @Resource
    private ProducerCheck1 producerCheck1;

    @Test
    void testCheck1() throws InterruptedException {
        producerCheck1.sendConfirm(RabbitMQConst.QUEUE_1, "测试消息发送");
        Thread.sleep(1000);
    }
}
