package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.example.util.CommonDiction.CONFIRM_EXCHANGE_NAME;
import static org.example.util.CommonDiction.CONFIRM_ROUTING_KEY;

/**
 * 用于Spring Boot发布确认的Controller
 */
@RestController
@RequestMapping("confirm")
@Slf4j
public class ProducerController {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @PostMapping("sendMsg01")
    public void testSendMsg(String message) {
        log.info("[S]Send message success! The message is : [{}]", message);
        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE_NAME, CONFIRM_ROUTING_KEY, message.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 发布确认
     * @param message
     */
    @PostMapping("sendMsg02")
    public void testSendMsgConfirm(String message) {
        log.info("[S]Send message success! The message is : [{}]", message);
        // 交换机发布确认的所需参数；用于传递信息给到RabbitTemplate.ConfirmCallback
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE_NAME,
                CONFIRM_ROUTING_KEY,
                message.getBytes(StandardCharsets.UTF_8),
                correlationData);
    }

    /**
     * 模拟发布确认失效的情况
     * 给到错误的交换机名称，模拟交换机宕机、异常
     * @param message
     */
    @PostMapping("sendMsg03")
    public void testSendMsgConfirmLossWhenExchangeDown(String message) {
        log.info("[S]Send message success! The message is : [{}]", message);
        // 交换机发布确认的所需参数；用于传递信息给到RabbitTemplate.ConfirmCallback
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE_NAME + "123",
                CONFIRM_ROUTING_KEY,
                message.getBytes(StandardCharsets.UTF_8),
                correlationData);
        /**
         * 执行结果：
         * Rabbit MQ报NOT_FOUND异常
         * Shutdown Signal: channel error; protocol method: #method<channel.close>(reply-code=404,
         * reply-text=NOT_FOUND - no exchange 'confirm.exchange123' in vhost '/hello', class-id=60, method-id=40)
         *
         * 实现的接口监听异常：
         * [Ex Err]Exchange is received loss, the message id is [150f092d-c4a5-40fa-a4e0-e3432c36d883],
         * and cause is [channel error; protocol method: #method<channel.close>(reply-code=404,
         * reply-text=NOT_FOUND - no exchange 'confirm.exchange123' in vhost '/hello', class-id=60, method-id=40)] ,
         * and correlationData is [{"future":{"cancelled":false,"done":true},"id":"150f092d-c4a5-40fa-a4e0-e3432c36d883"}]
         */
    }

    /**
     * 模拟发布确认失效的情况
     * 给到错误的队列名称，模拟队列宕机、异常
     * 一切正常，只是未被消费
     * @param message
     */
    @PostMapping("sendMsg04")
    public void testSendMsgConfirmLossWhenQueueDown(String message) {
        log.info("[S]Send message success! The message is : [{}]", message);
        // 交换机发布确认的所需参数；用于传递信息给到RabbitTemplate.ConfirmCallback
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE_NAME,
                CONFIRM_ROUTING_KEY + "123",
                message.getBytes(StandardCharsets.UTF_8),
                correlationData);
    }
}
