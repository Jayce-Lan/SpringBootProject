package com.example.raturn1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class ProducerReturn {
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 消息确认机制
     */
    private final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        /**
         * 消息队列生产/发送判定
         * @param correlationData 消息相关数据，一般用于获取唯一标识
         * @param sendFlag 是否发送成功
         * @param error 失败原因
         */
        @Override
        public void confirm(CorrelationData correlationData, boolean sendFlag, String error) {
            if (sendFlag) {
                log.info("return 消息发送成功确认 >>>>>>>>>> 消息id为: {}", correlationData.getId());
            } else {
                log.error("return 消息发送失败 >>>>>>>>>> 消息id为: {}, 失败原因为: {}", correlationData.getId(), error);
            }
        }
    };

    /**
     * 消息返回机制
     * 只有当无法匹配交换机/队列时才会进入此方法
     */
    private final RabbitTemplate.ReturnsCallback returnsCallback = new RabbitTemplate.ReturnsCallback() {
        @Override
        public void returnedMessage(ReturnedMessage returnedMessage) {
            log.info("returnedMessage >>>>>>>>>> {}", returnedMessage);
        }
    };

    public void sendReturn(String exchange, String routingKey, String msg) {
        rabbitTemplate.convertAndSend(exchange,
                routingKey,
                msg,
                new CorrelationData("" + System.currentTimeMillis()));
        // 使用上方的回调方法
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnsCallback(returnsCallback);
    }
}
