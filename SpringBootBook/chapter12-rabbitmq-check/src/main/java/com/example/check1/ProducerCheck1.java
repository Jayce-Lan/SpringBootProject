package com.example.check1;

import com.example.utils.RabbitMQConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 生产者设置好消息队列的确认机制
 */

@Component
@Slf4j
public class ProducerCheck1 {
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 配置消息确认机制
     */
    private final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        /**
         * 继承接口方法
         * @param correlationData 消息相关的数据，一般用于获取唯一标识id
         * @param sendMsgFlag 是否发送成功
         * @param error 失败原因
         */
        @Override
        public void confirm(CorrelationData correlationData, boolean sendMsgFlag, String error) {
            if (sendMsgFlag) {
                log.info("confirm 消息发送成功 >>>>>>>>>> 消息id为: {}", correlationData.getId());
            } else {
                log.error("confirm 消息发送失败 >>>>>>>>>> 消息id为: {}, 失败原因为: {}", correlationData.getId(), error);
            }
        }
    };

    /**
     * 发送消息，参数有交换机、空路由键、消息，并设置一个唯一的消息id
     * @param routingKey 路由键
     * @param msg 消息
     */
    public void sendConfirm(String routingKey, String msg) {
        rabbitTemplate.convertAndSend(RabbitMQConst.EX_CHANGE_1,
                routingKey,
                msg,
                new CorrelationData("" + System.currentTimeMillis()));
        // 使用上方配置的发送回调方法
        rabbitTemplate.setConfirmCallback(confirmCallback);
    }
}
