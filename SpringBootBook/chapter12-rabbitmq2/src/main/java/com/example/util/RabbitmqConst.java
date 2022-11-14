package com.example.util;


public class RabbitmqConst {
    /**
     * 简单模式：
     * 简单发送String信息
     */
    public static final String RABBIT_MQ_QUEUE = "rabbitmq_queue";
    /**
     * 简单模式：
     * 发送/接收对象
     */
    public static final String RABBIT_MQ_QUEUE_OBJECT = "rabbitmq_queue_object";
    /**
     * 工作队列模式：
     * 一个生产者对应多个消费者
     */
    public static final String RABBIT_MQ_QUEUE_ONE_TO_MORE = "rabbitmq_queue_one_to_more";
}
