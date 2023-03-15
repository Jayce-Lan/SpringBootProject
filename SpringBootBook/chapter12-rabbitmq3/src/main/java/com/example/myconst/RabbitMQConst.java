package com.example.myconst;

/**
 * 存储使用到的常量类
 */
public class RabbitMQConst {
    /**
     * 简单模式的消息名称
     */
    public static final String HELLO_QUEUE = "hello";

    /**
     * 工作队列（Work Queue/Task Queue 任务队列）
     */
    public static final String WORK_QUEUE = "work.hello";

    /**
     * 发布-订阅模式下的交换机
     */
    public static final String PUB_SUB_EX_CHANGE = "tut.fanout";

    /**
     * 路由模式下的交换机
     */
    public static final String ROUTING_EX_CHANGE = "tut.direct";
    /**
     * 路由模式下的路由键1：orange
     */
    public static final String ROUTING_ROUTING_KEY_ORANGE = "orange";
    /**
     * 路由模式下的路由键2：black
     */
    public static final String ROUTING_ROUTING_KEY_BLACK = "black";
    /**
     * 路由模式下的路由键3：green
     */
    public static final String ROUTING_ROUTING_KEY_GREEN = "green";
    /**
     * 路由模式下用于给到遍历的路由键数组
     */
    public static final String[] ROUTING_KEYS = {ROUTING_ROUTING_KEY_ORANGE, ROUTING_ROUTING_KEY_BLACK, ROUTING_ROUTING_KEY_GREEN};
}
