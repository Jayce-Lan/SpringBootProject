package com.example.check1;

import com.example.utils.RabbitMQConst;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class ConsumerCheck1 {
    /**
     * 值得注意的是，此处加入进入失败，那么生产者消息将会被堆积
     * 直至异常被处理后，将会重新消费，即：
     * 假如失败了3回，异常修复后，生产者再生产一条消息，那么消费者会消费4条消息（1+3）
     *
     * @param message
     * @param channel
     * @throws IOException
     */
    @RabbitListener(queues = RabbitMQConst.QUEUE_1)
    public void receiveConfirm(Message message, Channel channel) throws IOException {
        try {
            // 模拟接收失败
//            int a = 1 / 0;
            log.info("正常收到消息 >>>>>>>>>> {}", new String(message.getBody()));
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            // 两个布尔值，若第二个设为fales，则丢弃该消息；若设为true，则返回给队列
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            log.error("消费失败 >>>>>>>>> 我此次将返回给队列");
        }
    }
}
