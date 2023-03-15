package com.example.tut4;

import com.example.myconst.RabbitMQConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class Tut4Sender {
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private DirectExchange directExchange;

    AtomicInteger index = new AtomicInteger(0);
    AtomicInteger count = new AtomicInteger(0);

    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send() {
        StringBuilder builder = new StringBuilder("Hello to ");
        if (this.index.incrementAndGet() == 3) {
            this.index.set(0);
        }
        String routingKey = RabbitMQConst.ROUTING_KEYS[this.index.get()];
        builder.append(routingKey).append(' ');
        builder.append(this.count.incrementAndGet());
        String msg = builder.toString();
        rabbitTemplate.convertAndSend(directExchange.getName(), routingKey, msg);
        log.info("[x] Sent '{}'", msg);
    }
}
