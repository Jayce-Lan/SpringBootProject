package com.example.util;

import com.example.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 生产者
 * 其中方法1、2为简单模式
 */

@Component
@Slf4j
public class Producer {
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 如上面的示例所示，RabbitTemplate提供了convertAndSend方法发送消息。convertAndSend方法有routingKey和message两个参数：
     * 1）routingKey为要发送的路由地址。
     * 2）message为具体的消息内容。发送者和接收者的queuename必须一致，不然无法接收
     */
    public void produce() {
        String message = new Date() + "Nanning";
        log.info("生产者信息 >>>>>>>>>> {}", message);
        rabbitTemplate.convertAndSend(RabbitmqConst.RABBIT_MQ_QUEUE, message);
    }

    /**
     * 使用实体类作为生产/消费源
     * @param user
     */
    public void produceUser(User user) {
        log.info("生产者生产信息 >>>>>>>>>> {}", user);
        rabbitTemplate.convertAndSend(RabbitmqConst.RABBIT_MQ_QUEUE_OBJECT, user);
    }
}
