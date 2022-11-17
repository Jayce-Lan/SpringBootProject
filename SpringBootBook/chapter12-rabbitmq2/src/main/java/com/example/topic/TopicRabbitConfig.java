package com.example.topic;

import com.example.util.RabbitmqConst;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 发布订阅模式
 * Topic是RabbitMQ中灵活的一种方式，可以根据路由键绑定不同的队列。
 * Topic类型的Exchange与Direct相比，都可以根据路由键将消息路由到不同的队列。
 * 只不过Topic类型的Exchange可以让队列在绑定路由键时使用通配符。
 *
 * 有关通配符的规则为：
 * #：匹配一个或多个词。
 * *：只匹配一个词。
 */

@Configuration
public class TopicRabbitConfig {
    /**
     * 队列1
     * @return
     */
    @Bean
    public Queue queueMessage1() {
        return new Queue(RabbitmqConst.TOPIC_QUEUE_1);
    }

    /**
     * 队列2
     * @return
     */
    @Bean
    public Queue queueMessage2() {
        return new Queue(RabbitmqConst.TOPIC_QUEUE_2);
    }

    /**
     * 队列3
     * @return
     */
    @Bean
    public Queue queueMessage3() {
        return new Queue(RabbitmqConst.TOPIC_QUEUE_3);
    }

    /**
     * 交换机
     * @return
     */
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(RabbitmqConst.TOPIC_EXCHANGE);
    }

    @Bean
    public Binding bindingExchangeMessage1() {
        return BindingBuilder.bind(queueMessage1())
                .to(topicExchange())
                .with(RabbitmqConst.TOPIC_RULE_1);
    }

    @Bean
    public Binding bindingExchangeMessage2(Queue queueMessage2, TopicExchange topicExchange) {
        return BindingBuilder.bind(queueMessage2)
                .to(topicExchange)
                .with(RabbitmqConst.TOPIC_RULE_2);
    }

    @Bean
    public Binding bindingExchangeMessage3(Queue queueMessage3, TopicExchange topicExchange) {
        return BindingBuilder.bind(queueMessage3)
                .to(topicExchange)
                .with(RabbitmqConst.TOPIC_RULE_3);
    }
}
