package com.example.direct;

import com.example.util.RabbitmqConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Direct 路由模式消费者
 */
@Component
@Slf4j
public class ConsumerDirect {
    @RabbitHandler
    @RabbitListener(queuesToDeclare = @Queue(RabbitmqConst.DIRECT_Q1))
    public void processQ1(String message) {
        log.info("【消费者 {}】 >>>>>>>>> 消费信息为: {}", RabbitmqConst.DIRECT_Q1, message);
    }

    @RabbitHandler
    @RabbitListener(queuesToDeclare = @Queue(RabbitmqConst.DIRECT_Q2))
    public void processQ2(String message) {
        log.info("【消费者 {}】 >>>>>>>>> 消费信息为: {}", RabbitmqConst.DIRECT_Q2, message);
    }

    @RabbitHandler
    @RabbitListener(queuesToDeclare = @Queue(RabbitmqConst.DIRECT_Q3))
    public void processQ3(String message) {
        log.info("【消费者 {}】 >>>>>>>>> 消费信息为: {}", RabbitmqConst.DIRECT_Q3, message);
    }
}
