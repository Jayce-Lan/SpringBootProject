package org.example.hello;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import org.example.util.RabbitMQTestUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.example.util.RabbitMQConfigDiction.QUEUE_NAME;

/**
 * 消费者（接收者）
 */
public class Consumer {

    public static void main(String[] args) throws IOException, TimeoutException {
        Consumer consumer = new Consumer();
        consumer.testHelloWorldReceived();
    }

    /**
     * HelloWorld-简单模式，接收方（消费者）
     * @throws IOException
     * @throws TimeoutException
     */
    private void testHelloWorldReceived() throws IOException, TimeoutException {
        RabbitMQTestUtils rabbitMQTestUtils = new RabbitMQTestUtils();
        Connection connection = rabbitMQTestUtils.getConnectionFactory().newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println("[*] Waiting for messages. To exit press CTRL+C");

        // 额外的 DeliverCallback 接口，用于缓冲服务器推送给我们的信息。
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("[X] Received '" + message + "'");
        };
        /**
         * 消费者消费信息
         * 1.消费队列名称
         * 2.消费成功后是否自动应答，true标识自动，false手动
         * 3.消费者成功消费的回调
         * 4.消费者取消消息的回调（消费被中断）
         */
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
            System.out.println("Received is stop!");
        });
    }
}
