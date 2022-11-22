package com.example;

import com.example.raturn1.ProducerReturn;
import com.example.utils.RabbitMQConst;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
public class Return1Tests {
    @Resource
    private ProducerReturn producerReturn;

    @Test
    void testReturn() {
        producerReturn.sendReturn(RabbitMQConst.RETURN_EXCHANGE, RabbitMQConst.RETURN_QUEUE + "1", "测试数据");
    }
}
