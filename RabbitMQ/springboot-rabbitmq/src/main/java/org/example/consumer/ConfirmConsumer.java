package org.example.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

import static org.example.util.CommonDiction.CONFIRM_QUEUE_NAME;

@Component
@Slf4j
public class ConfirmConsumer {
    @RabbitListener(queues = CONFIRM_QUEUE_NAME)
    public void receivedTestMsg(Message message) {
        log.info("[R]Received success! The message is [{}], and routing key is [{}]", 
                new String(message.getBody(), StandardCharsets.UTF_8),
                message.getMessageProperties().getReceivedRoutingKey());
    }
}
