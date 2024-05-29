package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.nio.charset.StandardCharsets;

import static org.example.util.CommonDiction.PRIORITY_QUEUE_NAME;

/**
 * 其他知识点
 */
@RestController
@RequestMapping("other-controller")
@Slf4j
public class OtherController {
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 测试优先级队列
     * 优先级需要在队列中有消息积压的前提下才会产生
     * 如果发一条随即消费一条则无法实现
     */
    @GetMapping("testPriority")
    public void testPrioritySend() {
        for (int i = 0; i <= 15; i++) {
            String message = "info" + i;
            // 添加消息配置
            MessageProperties messageProperties = new MessageProperties();
            // 设置优先级
            messageProperties.setPriority(5);
            if (i % 5 == 0) {
                log.info("[S-P] Send message is success , the message is {}, and this is priority!", i);
                // 使用 rabbitTemplate.send可以承接上面设置好优先级的参数
                rabbitTemplate.send("", PRIORITY_QUEUE_NAME,
                        new Message(message.getBytes(StandardCharsets.UTF_8), messageProperties));
            } else {
                log.info("[S-N] Send message is success , the message is {}", i);
                rabbitTemplate.convertAndSend(PRIORITY_QUEUE_NAME, message.getBytes(StandardCharsets.UTF_8));
            }
        }
    }
}
