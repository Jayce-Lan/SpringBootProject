package com.example.raturn1;

import com.example.utils.RabbitMQConst;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class ConsumerRetrun {
    @RabbitListener(queues = RabbitMQConst.RETURN_QUEUE)
    public void receiveReturn(Message message, Channel channel) throws IOException {
        try {
            // 模拟接收失败
            log.info("return 正常收到消息 >>>>>>>>>> {}", new String(message.getBody()));
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            // 两个布尔值，若第二个设为fales，则丢弃该消息；若设为true，则返回给队列
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            log.error("return 消费失败 >>>>>>>>> 我此次将返回给队列");
        }
    }
}
