package com.example.topic;

import com.example.util.RabbitmqConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class ProducerTopic {
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 生产者
     * @param routingKey 消息传输键（用于识别发布给哪个订阅者，匹配规则）
     * @param message 消息
     */
    public void produce(String routingKey, String message) {
        log.info("【生产者】 >>>>>>>>>> routingKey: {}; message: {}", routingKey, message);
        rabbitTemplate.convertAndSend(RabbitmqConst.TOPIC_EXCHANGE, routingKey, message);
    }
}
