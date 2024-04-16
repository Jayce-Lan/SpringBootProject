package org.example.util;

import com.rabbitmq.client.ConnectionFactory;

/**
 * rabbitmq项目工具类
 */
public class RabbitMQTestUtils {
    /**
     * 创建连接工厂
     * @return 连接工厂
     */
    public ConnectionFactory getConnectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RabbitMQConfigDiction.MQ_HOST);
        factory.setVirtualHost(RabbitMQConfigDiction.VIRTUAL_HOST);
        factory.setUsername(RabbitMQConfigDiction.USER_NAME);
        factory.setPassword(RabbitMQConfigDiction.PASSWORD);
        return factory;
    }
}
