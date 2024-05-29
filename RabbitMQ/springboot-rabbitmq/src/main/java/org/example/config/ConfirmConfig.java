package org.example.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.example.util.CommonDiction.*;

/**
 * 发布确认Spring Boot
 * 绑定关系配置类
 * P ==> confirm.exchange; type: direct =key1=> confirm.queue ==> confirm consumer
 *
 * 备份交换机
 * 旨在对交换机进行一个备份，以便完成对未能投递出去对消息进行备份和预警
 */
@Configuration
public class ConfirmConfig {
    /**
     * 发布确认交换机
     * @return 发布确认交换机
     */
    @Bean
    public DirectExchange confirmExchange() {
//        return new DirectExchange(CONFIRM_EXCHANGE_NAME);
        // 由于需要做交换机备份，因此需要改造
        // 如果使用上面的new DirectExchange(CONFIRM_EXCHANGE_NAME)，那么durable默认为true，因此此处需要声明
        // withArgument(key, value)为单个键值对，withArguments(map)为map存储多个键值对
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE_NAME)
                .durable(true)
                .withArgument("alternate-exchange", BACKUP_EXCHANGE_NAME) // alternate-exchange 备份交换机
                .build();
    }

    /**
     * 发布确认队列
     * @return 发布确认队列
     */
    @Bean
    public Queue confirmQueue() {
        return new Queue(CONFIRM_QUEUE_NAME);
    }

    /**
     * 发布确认绑定
     * @param confirmQueue 队列
     * @param confirmExchange 交换机
     * @return 绑定关系
     */
    @Bean
    public Binding confirmQueueBindingConfirmExchange(Queue confirmQueue,
                                                      DirectExchange confirmExchange) {
        return BindingBuilder.bind(confirmQueue)
                .to(confirmExchange)
                .with(CONFIRM_ROUTING_KEY);
    }

    /**
     * 备份交换机
     * 以Fanout扇出形式将消息投递给backupQueue和warningQueue
     * @return 备份交换机
     */
    @Bean
    public FanoutExchange backupExchange() {
        return new FanoutExchange(BACKUP_EXCHANGE_NAME);
    }
    /**
     * 备份队列
     * @return 接收备份交换机传递的消息
     */
    @Bean
    public Queue backupQueue() {
        return new Queue(BACKUP_QUEUE_NAME);
    }
    /**
     * 报警队列
     * @return 接收备份交换机传递的消息
     */
    @Bean
    public Queue warningQueue() {
        return new Queue(BACKUP_WARNING_QUEUE_NAME);
    }

    /**
     * 绑定备份队列与备份交换机的关系
     * @param backupQueue 备份队列
     * @param backupExchange 备份交换机
     * @return 绑定关系
     */
    @Bean
    Binding backupQueueBindingBackupExchange(Queue backupQueue,
                                             FanoutExchange backupExchange) {
        return BindingBuilder.bind(backupQueue).to(backupExchange);
    }
    /**
     * 绑定报警队列与备份交换机的关系
     * @param warningQueue 报警队列
     * @param backupExchange 备份交换机
     * @return 绑定关系
     */
    @Bean
    Binding warningQueueBindingBackupExchange(Queue warningQueue,
                                             FanoutExchange backupExchange) {
        return BindingBuilder.bind(warningQueue).to(backupExchange);
    }
}
