package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

import static org.example.util.TtlCommonDiction.*;

/**
 * 生产者
 * 负责发送消息
 */
@RestController
@RequestMapping("/ttl")
@Slf4j
public class SentMsgController {
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 队列TTL的消息发送
     * @param message 发送消息
     */
    @PostMapping("sendMsg01")
    public void sendMsg01(String message) {
        log.info("[X]The message is : [{}] that is send success!", message);
        rabbitTemplate.convertAndSend(EXCHANGE_X, ROUTING_KEY_XA, message.getBytes(StandardCharsets.UTF_8));
        rabbitTemplate.convertAndSend(EXCHANGE_X, ROUTING_KEY_XB, message.getBytes(StandardCharsets.UTF_8));
    }
}
