package com.example.fanout;

import com.example.util.RabbitmqConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 广播模式消费者
 */
@Component
@Slf4j
public class ConsumerFanout {
    @RabbitHandler
    @RabbitListener(queues = RabbitmqConst.FANOUT_Q1)
    public void processQ1(String message) {
        log.info("消费者[{}]接收消息 >>>>>>>>>> {}", RabbitmqConst.FANOUT_Q1, message);
    }

    @RabbitHandler
    @RabbitListener(queues = RabbitmqConst.FANOUT_Q2)
    public void processQ2(String message) {
        log.info("消费者[{}]接收消息 >>>>>>>>>> {}", RabbitmqConst.FANOUT_Q2, message);
    }

    @RabbitHandler
    @RabbitListener(queues = RabbitmqConst.FANOUT_Q3)
    public void processQ3(String message) {
        log.info("消费者[{}]接收消息 >>>>>>>>>> {}", RabbitmqConst.FANOUT_Q3, message);
    }
}
