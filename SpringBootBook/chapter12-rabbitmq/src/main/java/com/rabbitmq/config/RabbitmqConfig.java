package com.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {
    private final String EXCHANGE_NAME = "topic_exchange_1";
    private final String QUEUE_NAME = "queue_1";

    @Bean("topocExchange1")
    public Exchange getExchange() {
        return ExchangeBuilder
                .topicExchange(EXCHANGE_NAME)
                .durable(true) // 是否持久化，true为是，会存储至磁盘，false只会存储在内存
                .build();
    }

    /**
     * 创建队列
     * @return
     */
    @Bean("queue1")
    public Queue getQueue() {
        return new Queue(QUEUE_NAME);
    }

    @Bean
    public Binding bindMessageQueue(@Qualifier("topocExchange1") Exchange exchange, @Qualifier("queue1") Queue queue) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("#.message.#")
                .noargs();
    }
}