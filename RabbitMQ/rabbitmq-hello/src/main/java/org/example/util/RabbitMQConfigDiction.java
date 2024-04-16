package org.example.util;

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
     * 相当于SQL的数据库，本次使用的为-/hello
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
}
