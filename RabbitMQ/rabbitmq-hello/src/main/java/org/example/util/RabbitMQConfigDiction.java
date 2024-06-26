package org.example.util;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

/**
 * Rabbit MQ 配置信息的字典
 * 用于存储配置信息
 */
public class RabbitMQConfigDiction {
    /**
     * 连接地址
     */
    public static final String MQ_HOST = "127.0.0.1";
    /**
     * VirtualHost
     * 相当于SQL的数据库，本次使用的为/hello
     */
    public static final String VIRTUAL_HOST = "/hello";
    /**
     * 用户名
     */
    public static final String USER_NAME = "guest";
    /**
     * 密码
     */
    public static final String PASSWORD = "guest";

    /**
     * 队列名称：Hello World的队列-hello
     */
    public static final String QUEUE_NAME = "hello";
    /**
     * 队列名称：TASK_QUEUE，工作队列
     */
    public static final String TASK_QUEUE = "task_queue_new";
    /**
     * 队列名称：TASK_ACK_QUEUE，手动应答工作队列
     */
    public static final String TASK_ACK_QUEUE = "ack_queue";
    /**
     * 队列名称：TASK_DURABLE_QUEUE，持久化队列
     */
    public static final String TASK_DURABLE_QUEUE = "durable_queue";
    /**
     * 队列名称：DIRECT_CONSOLE_QUEUE，路由模式console队列
     */
    public static final String DIRECT_CONSOLE_QUEUE = "console";
    /**
     * 队列名称：DIRECT_DISK_QUEUE，路由模式disk队列
     */
    public static final String DIRECT_DISK_QUEUE = "disk";
    /**
     * 死信队列-正常消费者的队列名称
     */
    public static final String NORMAL_QUEUE = "normal_queue";
    /**
     * 死信队列-死信消费者的队列名称
     */
    public static final String DEAD_QUEUE = "dead_queue";

    /**
     * 批量发送消息的条数
     */
    public static final int MESSAGE_COUNT = 1000;

    /**
     * Fanout扇出类型的交换机名称
     */
    public static final String FANOUT_EXCHANGE_NAME = "log";
    /**
     * Direct路由类型/直接类型的交换机名称
     */
    public static final String DIRECT_EXCHANGE_NAME = "direct_log";
    /**
     * Topics主题模式的交换机名称
     */
    public static final String TOPICS_EXCHANGE_NAME = "topics_log";
    /**
     * 死信队列-正常消费者所用交换机名称
     */
    public static final String NORMAL_EXCHANGE_NAME = "normal_exchange";
    /**
     * 死信队列-死信消费者所用交换机名称
     */
    public static final String DEAD_EXCHANGE_NAME = "dead_exchange";

    /**
     * 消费者消费成功的普通回调方法
     */
    public static final DeliverCallback RECEIVED_SUCCESS_CALL_BACK = (consumerTag, delivery) ->
        System.out.println("[X] Received '" + new String(delivery.getBody(), StandardCharsets.UTF_8) + "'");
    /**
     * Topics模式下成功回调
     * 使用 message.getEnvelope().getRoutingKey() 获取规则，即 RoutingKey
     */
    public static final DeliverCallback TOPICS_RECEIVED_SUCCESS_CALL_BACK = (consumerTag, message) ->
        System.out.println("[X] Received ... The message is [" + new String(message.getBody(), StandardCharsets.UTF_8)
                + "] And the RoutingKey type is [" + message.getEnvelope().getRoutingKey() + "]");
    /**
     * 消费者消费失败的回调方法
     */
    public static final CancelCallback RECEIVED_CANCEL_CALL_BACK = consumerTag ->
            System.out.println(consumerTag + "Received is cancel!");
}
