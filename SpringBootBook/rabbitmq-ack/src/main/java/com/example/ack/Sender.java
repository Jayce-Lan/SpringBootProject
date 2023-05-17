package com.example.ack;

import com.example.consts.DictConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import javax.annotation.Resource;

@Slf4j
public class Sender {
    @Resource
    private RabbitTemplate rabbitTemplate;

    public void send(String msg) {
        rabbitTemplate.convertAndSend(DictConst.ACK_EXCHANGE_NAME, DictConst.ACK_ROUTING_KEY, msg);
        log.info("发送消息: {}", msg);
    }
}
