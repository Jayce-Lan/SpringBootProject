package org.example.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static org.example.util.CommonDiction.*;

/**
 * 队列TTL
 * 用于绑定队列TTL演示的交换机与队列声明、绑定关系的类
 * 关系概览：
 *                        / routingKey: XA-> queue_QA:TTL 10s - routingKey: YD\
 * P -> exchange_X:direct                                                      > exchange_Y:direct - routingKey: YD -> queue_QD -> C
 *                        \ routingKey: XB-> queue_QB:TTL 40s - routingKey: YD/
 */
@Configuration
public class TtlQueueConfig {
    /**
     * 普通交换机 exchange_X
     * @return 普通交换机
     */
    @Bean("xExchange")
    public DirectExchange xExchange() {
        return new DirectExchange(EXCHANGE_X);
    }

    /**
     * 死信交换机 exchange_Y
     * @return 死信交换机
     */
    @Bean("yDlExchange")
    public DirectExchange yDlExchange() {
        return new DirectExchange(DEAD_LETTER_EXCHANGE_Y);
    }

    /**
     * TTL为10s的普通队列
     * @return TTL为10s的普通队列
     */
    @Bean("queueQA")
    public Queue queueQA() {
        Map<String, Object> arguments = new HashMap<>(3);
        // 正常队列声明设置死信交换机
        arguments.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE_Y);
        // 设置死信RoutingKey
        arguments.put("x-dead-letter-routing-key", ROUTING_KEY_YD);
        arguments.put("x-message-ttl", 10000);
        return QueueBuilder // 构建一个队列
                .durable(QUEUE_A) // 队列名称
                .withArguments(arguments) // 相当于Java声明中的参数map
                .build();
    }
    /**
     * TTL为40s的普通队列
     * @return TTL为10s的普通队列
     */
    @Bean("queueQB")
    public Queue queueQB() {
        Map<String, Object> arguments = new HashMap<>(3);
        arguments.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE_Y);
        arguments.put("x-dead-letter-routing-key", ROUTING_KEY_YD);
        arguments.put("x-message-ttl", 40000);
        return QueueBuilder.durable(QUEUE_B).withArguments(arguments)
                .build();
    }
    /**
     * 不设置TTL，交由生产者进行设置
     * @return 无TTL的队列
     */
    @Bean("queueQC")
    public Queue queueQC() {
        Map<String, Object> arguments = new HashMap<>(2);
        arguments.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE_Y);
        arguments.put("x-dead-letter-routing-key", ROUTING_KEY_YD);
        return QueueBuilder.durable(QUEUE_C).withArguments(arguments)
                .build();
    }

    /**
     * 死信队列
     * @return
     */
    @Bean("queueDlQD")
    public Queue queueDlQD() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE_D).build();
    }

    /**
     * 队列QA与交换机X绑定
     * @return
     */
    @Bean
    public Binding queueQABindingExchangeX() {
        return BindingBuilder.bind(queueQA())
                .to(xExchange())
                .with(ROUTING_KEY_XA);
    }

    /**
     * 队列QB与交换机X绑定
     * @param queue 队列QB
     * @param exchange 交换机X
     * @return 返回绑定关系
     */
    @Bean
    public Binding queueQBBindingExchangeX(@Qualifier("queueQB")Queue queue,
                                           @Qualifier("xExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_XB);
    }

    /**
     * 队列QC与交换机X绑定
     * @param queue 队列QC
     * @param exchange 交换机X
     * @return 返回绑定关系
     */
    @Bean
    public Binding queueQCBindingExchangeX(@Qualifier("queueQC")Queue queue,
                                           @Qualifier("xExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_XC);
    }
    @Bean
    public Binding queueQDBindingExchangeY(@Qualifier("queueDlQD")Queue queue,
                                           @Qualifier("yDlExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_YD);
    }
}
