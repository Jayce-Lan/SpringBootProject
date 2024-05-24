package org.example.config.msgconfirm;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 用于队列确认的工具类
 * 重写了队列确认的方法 ConfirmCallback
 */
@Configuration
@Slf4j
    public class MyQueueReturnCallback implements RabbitTemplate.ReturnsCallback {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        rabbitTemplate.setReturnsCallback(this);
    }

    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.error("return message : {}", JSONObject.toJSONString(returnedMessage));
        // 对应的correlationData存储的消息id，可以在此处获取，
        // 可以理解为correlationData就是headers，只是以map进行存储了
        // 其中，消息的id被存储在了"spring_returned_message_correlation"当中
        String springReturnedMessageCorrelation = (String) returnedMessage.getMessage()
                .getMessageProperties()
                .getHeaders()
                .get("spring_returned_message_correlation");
        log.error("[Queue Err] Queue return loss! The message exchange is :[{}], and routing key is [{}], " +
                        "and the message correlationData id is [{}], please check the binding!",
                returnedMessage.getExchange(),
                returnedMessage.getRoutingKey(),
                springReturnedMessageCorrelation);
    }
}
