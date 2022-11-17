package com.example.topic;

import com.example.util.RabbitmqConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ConsumerTopic {
    @RabbitHandler
    @RabbitListener(queues = RabbitmqConst.TOPIC_QUEUE_1)
    public void processTopicQ1(String message) {
        log.info("消费者[{}]消费信息 >>>>>> {}, 规则为: {}",
                RabbitmqConst.TOPIC_QUEUE_1,
                message,
                RabbitmqConst.TOPIC_RULE_1);
    }

    @RabbitHandler
    @RabbitListener(queues = RabbitmqConst.TOPIC_QUEUE_2)
    public void processTopicQ2(String message) {
        log.info("消费者[{}]消费信息 >>>>>> {}, 规则为: {}",
                RabbitmqConst.TOPIC_QUEUE_2,
                message,
                RabbitmqConst.TOPIC_RULE_2);
    }

    @RabbitHandler
    @RabbitListener(queues = RabbitmqConst.TOPIC_QUEUE_3)
    public void processTopicQ3(String message) {
        log.info("消费者[{}]消费信息 >>>>>> {}, 规则为: {}",
                RabbitmqConst.TOPIC_QUEUE_3,
                message,
                RabbitmqConst.TOPIC_RULE_3);
    }
}
