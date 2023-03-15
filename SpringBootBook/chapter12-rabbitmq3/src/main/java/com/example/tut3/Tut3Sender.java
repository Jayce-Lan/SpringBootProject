package com.example.tut3;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class Tut3Sender {
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private FanoutExchange fanoutExchange;

    AtomicInteger dots = new AtomicInteger(0);
    AtomicInteger count = new AtomicInteger(0);

    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send() {
        StringBuilder builder = new StringBuilder("Hello");
        if (dots.getAndIncrement() == 3) {
            dots.set(1);
        }
        for (int i = 0; i < dots.get(); i++) {
            builder.append('.');
        }
        builder.append(count.incrementAndGet());
        String msg = builder.toString();
        /*
        当需要使用交换机时，第二个参数其实并不能为空，
        在tut1、tut2的例子中，由于第一位存储的是队列名（路由键），并且传入默认交换机中因此可以不需要第三位参数
         */
        rabbitTemplate.convertAndSend(fanoutExchange.getName(), "", msg);
        log.info("[x] Sent '{}'", msg);
    }
}
