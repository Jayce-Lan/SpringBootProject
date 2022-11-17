package com.example.fanout;

import com.example.util.RabbitmqConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 广播模式生产者
 */
@Component
@Slf4j
public class ProducerFanout {
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 使用Fanout广播模式无须指定routingKey，
     * 默认往交换机上的所有队列广播此消息
     * @param message 消息
     */
    public void produce(String message) {
        log.info("生产者消息 >>>>>>>>>> {}", message);
        rabbitTemplate.convertAndSend(RabbitmqConst.FANOUT_EXCHANGE, "", message);
    }
}
