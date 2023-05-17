package com.example.ack;

import com.example.consts.DictConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RabbitConfig {
    @Bean
    public DirectExchange ackExchange() {
        return new DirectExchange(DictConst.ACK_EXCHANGE_NAME);
    }

    @Bean
    public Queue ackQueue() {
        return new Queue(DictConst.ACK_QUEUE_NAME);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(ackQueue())
                .to(ackExchange())
                .with(DictConst.ACK_ROUTING_KEY);
    }

    @Bean
    public Receiver receiver() {
        return new Receiver();
    }
}
