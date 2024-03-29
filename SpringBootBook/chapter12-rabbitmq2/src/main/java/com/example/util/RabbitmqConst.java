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

    /**
     * 路由模式
     * 队列1
     */
    public static final String DIRECT_Q1 = "direct.Q1";
    /**
     * 路由模式
     * 队列2
     */
    public static final String DIRECT_Q2 = "direct.Q2";
    /**
     * 路由模式
     * 队列3
     */
    public static final String DIRECT_Q3 = "direct.Q3";
    /**
     * 路由模式，交换机名称
     */
    public static final String DIRECT_EXCHANGE_NAME = "directExchange";

    /**
     * 广播模式
     * 队列1
     */
    public static final String FANOUT_Q1 = "fanout.Q1";
    /**
     * 广播模式
     * 队列2
     */
    public static final String FANOUT_Q2 = "fanout.Q2";
    /**
     * 广播模式
     * 队列3
     */
    public static final String FANOUT_Q3 = "fanout.Q3";
    /**
     * 广播模式
     * 交换机名称
     */
    public static final String FANOUT_EXCHANGE = "fanoutExchange";

    /**
     * 发布订阅模式
     * 队列名称
     */
    public static final String TOPIC_QUEUE_1 = "topic.queue1";
    /**
     * 发布订阅模式
     * 队列名称
     */
    public static final String TOPIC_QUEUE_2 = "topic.queue2";
    /**
     * 发布订阅模式
     * 队列名称
     */
    public static final String TOPIC_QUEUE_3 = "topic.queue3";
    /**
     * 发布订阅模式
     * 交换机
     */
    public static final String TOPIC_EXCHANGE = "topicExchange";
    /**
     * 发布订阅模式
     * 规则1
     */
    public static final String TOPIC_RULE_1 = "topic.color.*";
    /**
     * 发布订阅模式
     * 规则2
     */
    public static final String TOPIC_RULE_2 = "topic.#";
    /**
     * 发布订阅模式
     * 规则3
     */
    public static final String TOPIC_RULE_3 = "topic.msg.*";
}
