package org.example.consumer;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

import static org.example.util.CommonDiction.PRIORITY_QUEUE_NAME;

@Component
@Slf4j
public class OtherConsumer {
    @RabbitListener(queues = PRIORITY_QUEUE_NAME)
    public void receivedPriorityMsg(Message message) {
        log.info("[R]Message received success, message is [{}] ==> {}",
                new String(message.getBody(), StandardCharsets.UTF_8),
                JSONObject.toJSONString(message));
    }
}
