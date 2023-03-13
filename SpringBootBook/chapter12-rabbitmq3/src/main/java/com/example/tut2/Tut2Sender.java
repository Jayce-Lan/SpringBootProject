package com.example.tut2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class Tut2Sender {
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private Queue queue;

    AtomicInteger dots = new AtomicInteger(0);
    AtomicInteger count = new AtomicInteger(0);

    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send() {
        StringBuilder builder = new StringBuilder("Hello");
        // 原子上增加一个当前值
        if (dots.getAndIncrement() == 4) {
            dots.set(1);
        }
        for (int i = 0; i < dots.get(); i++) {
            builder.append(".");
        }
        // 原子上增加一个当前值
        builder.append(count.incrementAndGet());
        String msg = builder.toString();
        rabbitTemplate.convertAndSend(queue.getName(), msg);
        log.info("[x] Sent '{}'", msg);
    }
}
