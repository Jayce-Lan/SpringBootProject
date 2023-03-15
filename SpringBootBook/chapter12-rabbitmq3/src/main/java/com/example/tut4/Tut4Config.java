package com.example.tut4;

import com.example.myconst.RabbitMQConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Routing-将使订阅消息的子集成为可能。
 * 例如，我们将能够只将消息定向到感兴趣的特定指向("orange", "black", "green")
 * 绑定是exChange和Queue之间的关系。这可以简单地理解为:Queue对来自此exChange的消息感兴趣
 */
@Profile({"tut4", "routing"})
@Configuration
@Slf4j
public class Tut4Config {

    /**
     * DirectExchange - 直接交换机
     * 绑定键的含义取决于exchange类型，之前使用的 FanoutExchange 就不适合此处
     * Fanout Exchange 只能进行无脑广播
     * 上一教程中的消息传递系统将所有消息广播给所有消费者。（FanoutExchange）
     * 我们希望扩展它，允许基于颜色类型过滤消息。（DirectExchange）
     * 例如，我们可能希望将日志消息写入磁盘的程序只接收严重错误，而不是在警告或信息日志消息上浪费磁盘空间。
     * @return
     */
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(RabbitMQConst.ROUTING_EX_CHANGE);
    }

    @Profile("receiver")
    private static class ReceiverConfig {
        @Bean
        public Queue autoDeleteQueue1() {
            return new AnonymousQueue();
        }

        @Bean
        public Queue autoDeleteQueue2() {
            return new AnonymousQueue();
        }

        /**
         * 绑定可以使用"with"接受额外的绑定键参数
         * 我们将交换和队列传递到BindingBuilder中，
         * 并简单地使用"with" 将bindingKey/routingKey为"orange"的键绑定到交换机"directExchange"上
         * 绑定键的含义取决于exchange类型，之前使用的 FanoutExchange 就不适合此处
         * @param directExchange
         * @param autoDeleteQueue1
         * @return
         */
        @Bean
        public Binding binding1a(DirectExchange directExchange, Queue autoDeleteQueue1) {
            return BindingBuilder.bind(autoDeleteQueue1)
                    .to(directExchange)
                    .with(RabbitMQConst.ROUTING_ROUTING_KEY_ORANGE);
        }

        /**
         * 值的注意的是，路由模式下，当同一个路由键被不同的队列绑定，
         * 那么，生产者一旦生产了符合路由键的消息，那么，两个消费者都会消费
         * 一次生产，多次消费（对不同的消费者而言）
         * @param directExchange
         * @param autoDeleteQueue1
         * @return
         */
        @Bean
        public Binding binding1b(DirectExchange directExchange, Queue autoDeleteQueue1) {
            return BindingBuilder.bind(autoDeleteQueue1)
                    .to(directExchange)
                    .with(RabbitMQConst.ROUTING_ROUTING_KEY_BLACK);
        }

        @Bean
        public Binding binding2a(DirectExchange directExchange, Queue autoDeleteQueue2) {
            return BindingBuilder.bind(autoDeleteQueue2)
                    .to(directExchange)
                    .with(RabbitMQConst.ROUTING_ROUTING_KEY_GREEN);
        }

        @Bean
        public Binding binding2b(DirectExchange directExchange, Queue autoDeleteQueue2) {
            return BindingBuilder.bind(autoDeleteQueue2)
                    .to(directExchange)
                    .with(RabbitMQConst.ROUTING_ROUTING_KEY_BLACK);
        }

        @Bean
        public Tut4Receiver receiver() {
            return new Tut4Receiver();
        }
    }

    @Profile("sender")
    @Bean
    public Tut4Sender sender() {
        return new Tut4Sender();
    }
}
