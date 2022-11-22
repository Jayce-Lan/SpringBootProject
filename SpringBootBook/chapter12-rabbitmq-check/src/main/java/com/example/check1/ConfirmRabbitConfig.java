package com.example.check1;

import com.example.utils.RabbitMQConst;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfirmRabbitConfig {
    @Bean
    public Queue confirmQueue() {
        return new Queue(RabbitMQConst.QUEUE_1);
    }

    @Bean
    public DirectExchange confirmExchange() {
        return new DirectExchange(RabbitMQConst.EX_CHANGE_1);
    }

    @Bean
    public Binding confirmFanoutExchangeBing(Queue confirmQueue, DirectExchange confirmExchange) {
        return BindingBuilder.bind(confirmQueue)
                .to(confirmExchange)
                .with(RabbitMQConst.QUEUE_1);
    }
}
