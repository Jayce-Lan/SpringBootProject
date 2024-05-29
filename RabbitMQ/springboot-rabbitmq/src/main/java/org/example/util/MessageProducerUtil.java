package org.example.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * 存储一些自定义属性的生产者类
 * 例如需要声明队列优先级等信息
 */
@Configuration
@Slf4j
public class MessageProducerUtil {
    @Resource
    private RabbitTemplate rabbitTemplate;

    public void sendPropertiesMessage(String exchange, String routingKey, String message, int priority) {
        // 设置配置类
        MessageProperties messageProperties = new MessageProperties();
        // 配置生产者发送消息优先级
        messageProperties.setPriority(priority);
        rabbitTemplate.send(exchange, routingKey, new Message(message.getBytes(StandardCharsets.UTF_8), messageProperties));
    }
}
