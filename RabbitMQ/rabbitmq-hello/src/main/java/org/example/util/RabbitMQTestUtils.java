package org.example.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

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

    /**
     * 直接获取信道
     * @return 返回MQ设置好的的信道
     */
    public static Channel getChannel() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RabbitMQConfigDiction.MQ_HOST);
        factory.setVirtualHost(RabbitMQConfigDiction.VIRTUAL_HOST);
        factory.setUsername(RabbitMQConfigDiction.USER_NAME);
        factory.setPassword(RabbitMQConfigDiction.PASSWORD);
        return factory.newConnection().createChannel();
    }

    /**
     * 模拟事务处理时间
     * @param second 所需时间，单位秒
     */
    public static void getSleep(int second) {
        try {
            Thread.sleep(second * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
