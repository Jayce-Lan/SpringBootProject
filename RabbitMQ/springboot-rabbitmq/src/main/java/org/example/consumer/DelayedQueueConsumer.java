package org.example.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.example.util.CommonDiction;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * 基于插件的延迟队列消费者
 */
@Component
@Slf4j
public class DelayedQueueConsumer {
    @RabbitListener(queues = CommonDiction.DELAYED_QUEUE_NAME)
    public void receivedDelayedMsg(Message message, Channel channel) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("[X] The message is received, body is [{}]", msg);
    }
}
