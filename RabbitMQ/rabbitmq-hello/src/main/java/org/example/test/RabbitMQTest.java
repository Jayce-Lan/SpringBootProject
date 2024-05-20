package org.example.test;

import org.example.util.RabbitMQTestUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 用于测试一些方法的类
 */
public class RabbitMQTest {
    public static void main(String[] args) throws IOException, TimeoutException {
        RabbitMQTestUtils.deleteDurableQueue("normal_queue");
    }

    /**
     * 测试Rabbit MQ官方提供的创建临时队列的方法
     */
    private void testGetQueue() throws IOException, TimeoutException {
        System.out.println(RabbitMQTestUtils.getChannel().queueDeclare().getQueue());
    }
}
