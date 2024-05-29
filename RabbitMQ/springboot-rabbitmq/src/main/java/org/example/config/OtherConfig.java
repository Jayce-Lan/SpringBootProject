package org.example.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static org.example.util.CommonDiction.PRIORITY_QUEUE_NAME;

/**
 * 关于其他知识点的绑定关系
 */
@Configuration
@Slf4j
public class OtherConfig {
    @Bean
    public Queue priorityQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-max-priority", Integer.valueOf(5));
        return QueueBuilder.durable(PRIORITY_QUEUE_NAME)
                .withArguments(arguments)
                .build();
    }
}
