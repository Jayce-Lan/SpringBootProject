package org.example.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

import static org.example.util.TtlCommonDiction.DEAD_LETTER_QUEUE_D;

/**
 * 队列TTL - 消费者
 *
 */
@Component
@Slf4j
public class DeadLetterQueueConsumer {
    @RabbitListener(queues = DEAD_LETTER_QUEUE_D)
    public void receivedQDMsg(Message message, Channel channel) {
        log.info("[X]Received message success, the message is [{}], and routing Key is [{}]",
                new String(message.getBody(), StandardCharsets.UTF_8),
                message.getMessageProperties().getReceivedRoutingKey());
    }
}
