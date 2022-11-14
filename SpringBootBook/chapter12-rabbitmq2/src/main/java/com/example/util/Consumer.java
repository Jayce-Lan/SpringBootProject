package com.example.util;

import com.example.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 消费者
 * 其中方法1、2为简单模式
 */
@Component
@Slf4j
public class Consumer {
    /**
     * Consumer消费者通过@RabbitListener注解创建侦听器端点，绑定rabbitmq_queue队列
     * @RabbitListener注解提供了@QueueBinding、@Queue、@Exchange等对象，通过这个组合注解配置交换机、绑定路由并且配置监听功能等
     * @RabbitHandler注解为具体接收的方法
     * @param message
     */
    @RabbitHandler
    @RabbitListener(queuesToDeclare = @Queue(RabbitmqConst.RABBIT_MQ_QUEUE))
    public void process(String message) {
        log.info("消费者消费信息 >>>>>>>>>>>>>>>> {}", message);
    }

    /**
     * 消费实体类
     * @param user
     */
    @RabbitHandler
    @RabbitListener(queuesToDeclare = @Queue(RabbitmqConst.RABBIT_MQ_QUEUE_OBJECT))
    public void processUser(User user) {
        log.info("消费者消费信息 >>>>>>>>>>>>>>>> {}", user);
    }
}
