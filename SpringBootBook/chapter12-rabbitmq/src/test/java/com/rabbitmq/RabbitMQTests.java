package com.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
public class RabbitMQTests {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Test
    void testSendMessage() {
        rabbitTemplate.convertAndSend("topic_exchange_1", "message", "发送消息");
    }
}
