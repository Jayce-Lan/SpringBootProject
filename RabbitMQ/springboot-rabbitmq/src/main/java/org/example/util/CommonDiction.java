package org.example.util;

public class CommonDiction {
    /**
     * 普通交换机 exchange_X
     * 用于将消息按照RoutingKey分发到不同TTL的交换机中
     */
    public static final String EXCHANGE_X = "exchange_x";
    /**
     * 死信交换机 exchange_Y
     * 用于将消息发送到队列queue_QD中
     */
    public static final String DEAD_LETTER_EXCHANGE_Y = "exchange_y_dl";
    /**
     * 普通队列 queue_a
     * TTL为10s
     */
    public static final String QUEUE_A = "queue_qa";
    /**
     * 绑定QA的Routing Key
     */
    public static final String ROUTING_KEY_XA = "xa";
    /**
     * 普通队列 queue_b
     * TTL为40s
     */
    public static final String QUEUE_B = "queue_qb";
    /**
     * 绑定QB的Routing Key
     */
    public static final String ROUTING_KEY_XB = "xb";
    /**
     * 普通队列 queue_c
     * 不设置TTL延迟，交由生产者进行设置
     */
    public static final String QUEUE_C = "queue_qc";
    /**
     * 绑定QC的Routing Key
     */
    public static final String ROUTING_KEY_XC = "xc";
    /**
     * 死信队列 queue_b
     */
    public static final String DEAD_LETTER_QUEUE_D = "queue_qd_dl";
    /**
     * 绑定QD的Routing Key
     */
    public static final String ROUTING_KEY_YD = "yd";

    /**
     * 用于交换机延迟的队列名
     */
    public static final String DELAYED_QUEUE_NAME = "delayed.queue";
    /**
     * 用于交换机延迟的交换机名
     */
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    /**
     * 用于交换机延迟的routing key
     */
    public static final String DELAYED_ROUTING_KEY = "delayed.routing_key";
    /**
     * 安装了 rabbitmq_delayed_message_exchange 插件后
     * 作为交换机延迟的交换机类型，由插件新增的一个类型
     */
    public static final String X_DELAYED_MESSAGE_EXCHANGE_TYPE = "x-delayed-message";

    /**
     * 用于SpringBoot发布确认的交换机
     */
    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";
    /**
     * 用于SpringBoot发布确认的队列
     */
    public static final String CONFIRM_QUEUE_NAME = "confirm.queue";
    /**
     * 用于SpringBoot发布确认的routing key
     */
    public static final String CONFIRM_ROUTING_KEY = "confirm.key";

}
