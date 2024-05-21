package org.example.util;

public class TtlCommonDiction {
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
     * 死信队列 queue_b
     */
    public static final String DEAD_LETTER_QUEUE_D = "queue_qd_dl";
    /**
     * 绑定QD的Routing Key
     */
    public static final String ROUTING_KEY_YD = "yd";
}
