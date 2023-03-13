package com.example.tut1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

@Slf4j
public class Tut1Sender {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private Queue queue;

    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send() {
        String msg = "Hello, World - 20230313 11";
        this.rabbitTemplate.convertAndSend(queue.getName(), msg);
        log.info("[x] Sent '{}'", msg);
    }
}
