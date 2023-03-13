package com.example.tut1;

import com.example.myconst.RabbitMQConst;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * 简单模式（一对一的消息队列收发）
 */
@Profile({"tut1", "hello-world"})
@Configuration
public class Tut1Config {
    @Bean
    public Queue hello() {
        return new Queue(RabbitMQConst.HELLO_QUEUE);
    }

    @Profile("receiver")
    @Bean
    public Tut1Receiver receiver() {
        return new Tut1Receiver();
    }

    @Profile("sender")
    @Bean
    public Tut1Sender sender() {
        return new Tut1Sender();
    }
}
