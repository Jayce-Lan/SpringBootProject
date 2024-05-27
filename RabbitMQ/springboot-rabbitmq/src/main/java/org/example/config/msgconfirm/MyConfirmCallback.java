package org.example.config.msgconfirm;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Configuration
@Slf4j
public class MyConfirmCallback implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    /**
     * 实现 RabbitTemplate.ConfirmCallback 接口
     * 交换机确认回调方法
     * @param correlationData 消息内容，存储消息id及相关信息
     * @param ack 接收确认成功true；失败false
     * @param cause 存储失败原因，成功为null；失败为false
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String messageId = "";
        if (correlationData != null) {
            messageId = StringUtils.hasText(correlationData.getId()) ? correlationData.getId(): "";
        }
        if (ack) {
            log.info("[Ex]Exchange is received success, the message id is [{}], and correlationData is [{}]",
                    messageId, JSONObject.toJSONString(correlationData));
        } else {
            log.error("[Ex Err]Exchange is received loss, the message id is [{}], and cause is [{}] , and correlationData is [{}]",
                    messageId, cause, JSONObject.toJSONString(correlationData));
        }
    }

    /**
     * 实现 RabbitTemplate.ReturnsCallback 接口
     * 队列确认回调方法
     * @param returnedMessage 存储队列未接收到消息的信息，如RoutingKey、交换机等
     */
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
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
