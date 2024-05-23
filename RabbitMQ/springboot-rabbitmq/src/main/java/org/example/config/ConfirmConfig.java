package org.example.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.example.util.CommonDiction.*;

/**
 * 发布确认Spring Boot
 * 绑定关系配置类
 * P ==> confirm.exchange; type: direct =key1=> confirm.queue ==> confirm consumer
 */
@Configuration
public class ConfirmConfig {
    @Bean
    public DirectExchange confirmExchange() {
        return new DirectExchange(CONFIRM_EXCHANGE_NAME);
    }

    @Bean
    public Queue confirmQueue() {
        return new Queue(CONFIRM_QUEUE_NAME);
    }

    @Bean
    public Binding confirmQueueBindingConfirmExchange(Queue confirmQueue,
                                                      DirectExchange confirmExchange) {
        return BindingBuilder.bind(confirmQueue)
                .to(confirmExchange)
                .with(CONFIRM_ROUTING_KEY);
    }
}
