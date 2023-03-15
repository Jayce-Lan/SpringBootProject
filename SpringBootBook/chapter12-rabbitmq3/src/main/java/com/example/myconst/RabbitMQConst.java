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
}
