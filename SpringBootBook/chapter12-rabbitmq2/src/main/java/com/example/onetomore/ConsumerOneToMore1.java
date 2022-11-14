package com.example.onetomore;

import com.example.util.RabbitmqConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 工作队列模式，一个生产者对应多个消费者
 * 消费者1
 */

@Component
@Slf4j
public class ConsumerOneToMore1 {
    @RabbitHandler
    @RabbitListener(queuesToDeclare = @Queue(RabbitmqConst.RABBIT_MQ_QUEUE_ONE_TO_MORE))
    public void process(String message) {
        log.info("消费者[1]消费队列 >>>>>>>>>>>>> {}", message);
    }
}
