package com.example.tut5;

import com.example.myconst.RabbitMQConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class Tut5Sender {
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private TopicExchange topicExchange;

    AtomicInteger index = new AtomicInteger(0);
    AtomicInteger count = new AtomicInteger(0);

    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send() {
        StringBuilder builder = new StringBuilder("Hello to ");
        if (index.incrementAndGet() == RabbitMQConst.TOPIC_ROUTING_KEYS.length) {
            index.set(0);
        }
        String routingKey = RabbitMQConst.TOPIC_ROUTING_KEYS[index.get()];
        builder.append(routingKey).append(' ');
        builder.append(count.incrementAndGet());
        String msg = builder.toString();
        rabbitTemplate.convertAndSend(topicExchange.getName(), routingKey, msg);
        log.info("{} [x] Sent '{}'", topicExchange.getName(), msg);
    }
}
