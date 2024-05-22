package org.example.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.SendMsgAndTimeDTO;
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

    /**
     * 由生产者指定TTL的方法
     * @param sendMsgAndTimeDTO 存储消息及TTL时间
     */
    @PostMapping("sendMsg02")
    public void sendExpirationMsg02(SendMsgAndTimeDTO sendMsgAndTimeDTO) {
        log.info(JSONObject.toJSONString(sendMsgAndTimeDTO));
        log.info("[X]The message is send, message is [{}], and TTL is [{}]",
                sendMsgAndTimeDTO.getMessage(),
                sendMsgAndTimeDTO.getTtlTime());
        rabbitTemplate.convertAndSend(EXCHANGE_X, ROUTING_KEY_XC,
                sendMsgAndTimeDTO.getMessage().getBytes(StandardCharsets.UTF_8),
                message -> {
                    // 设置超时时间
                    message.getMessageProperties().setExpiration(String.valueOf(sendMsgAndTimeDTO.getTtlTime()));
                    return message;
                });
    }
}
