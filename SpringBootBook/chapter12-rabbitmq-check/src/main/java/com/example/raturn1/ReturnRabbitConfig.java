package com.example.raturn1;

import com.example.utils.RabbitMQConst;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReturnRabbitConfig {
    @Bean
    public Queue returnQueue() {
        return new Queue(RabbitMQConst.RETURN_QUEUE);
    }

    @Bean
    public DirectExchange returnExchange() {
        return new DirectExchange(RabbitMQConst.RETURN_EXCHANGE);
    }

    @Bean
    public Binding returnBind() {
        return BindingBuilder.bind(returnQueue())
                .to(returnExchange())
                .with(RabbitMQConst.RETURN_QUEUE);
    }
}
