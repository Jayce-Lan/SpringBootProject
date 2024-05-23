package org.example.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static org.example.util.CommonDiction.*;

/**
 * 用于存储交换机设置TTL的绑定关系
 * 使用了 rabbitmq_delayed_message_exchange 插件
 * 传递关系：
 * P ==> delayed.exchange;type: direct =delay.routingkey => delayed.queue ==> C
 */
@Configuration
public class DelayedQueueConfig {
    /**
     * 配置队列
     * @return
     */
    @Bean
    public Queue delayedQueue() {
        return new Queue(DELAYED_QUEUE_NAME);
    }

    /**
     * 基于插件配置自定义交换机
     * @return CustomExchange 返回一个自定义类型的交换机
     */
    @Bean
    public CustomExchange delayedExchange() {
        Map<String, Object> arguments = new HashMap<>();
        // 延迟类型，直接类型，因为RoutingKey是个固定值
        // 值得注意的是，这里的 "direct"需要使用字符串，尝试使用常量，因为不是String，因此导致交换机与队列创建失败
        arguments.put("x-delayed-type", "direct");
        /**
         * 自定义交换机
         * 1.交换机名称
         * 2.交换机类型
         * 3.是否持久化
         * 4.是否自动删除
         * 5.其他参数
         */
        return new CustomExchange(DELAYED_EXCHANGE_NAME,
                X_DELAYED_MESSAGE_EXCHANGE_TYPE, // x-delayed-message
                true,
                false,
                arguments);
    }

    /**
     * 队列与交换机绑定
     * @return
     */
    @Bean
    public Binding delayedQueueBindingDelayedExchange(Queue delayedQueue, Exchange delayedExchange) {
        return BindingBuilder.bind(delayedQueue)
                .to(delayedExchange)
                .with(DELAYED_ROUTING_KEY)
                .noargs();
    }
}
