package com.rabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 消费者
 */
@Component
@Slf4j
public class Consumer {
    @RabbitListener(queues = "queue_1")
    public void listenMessage(Message message) {
        log.info("接收消息：{}", message);
    }
}
