package com.example.direct;

import com.example.util.RabbitmqConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * direct 路由模式生产者
 */

@Component
@Slf4j
public class ProducerDirect {
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 向生产者发起请求，生产内容
     * direct路由模式时，传入了具体的routingKey参数。
     * 这样RabbitMQ将消息发送到对应的交换机，
     * 交换机再通过消息的routingKey匹配队列绑定的bindingKey，从而实现消息路由传递的功能
     * @param routingKey 对应的路由key
     * @param context 消息内容
     */
    public void produce(String routingKey, String context) {
        log.info("【生产者】>>>>>>>>>>>>> routingKey: {}; 队列消息内容为: {}", routingKey, context);
        /**
         * @参数1 交换机名称
         * @参数2 路由键
         * @参数3 消息内容
         */
        rabbitTemplate.convertAndSend(RabbitmqConst.DIRECT_EXCHANGE_NAME, routingKey, context);
    }
}
