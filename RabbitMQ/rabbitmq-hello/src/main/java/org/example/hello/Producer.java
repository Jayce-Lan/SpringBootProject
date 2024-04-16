package org.example.hello;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.example.util.RabbitMQTestUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import static org.example.util.RabbitMQConfigDiction.QUEUE_NAME;

/**
 * 生产者（发送方）
 * 发送消息
 */
public class Producer {
    // 发消息
    public static void main(String[] args) {
        Producer producer = new Producer();
        producer.testHelloWorldSent();
    }

    /**
     * HelloWorld-简单模式，发送方（生产者）
     */
    private void testHelloWorldSent() {
        // 生产者创建连接-获取信道（Channel）
        RabbitMQTestUtils rabbitMQTestUtils = new RabbitMQTestUtils();
        try (Connection connection = rabbitMQTestUtils.getConnectionFactory().newConnection();
             Channel channel = connection.createChannel()){
            /**
             * 生成一个队列
             * 1.队列名称
             * 2.队列里消息是否需要持久化（写入磁盘），默认存在内存中，即false
             * 3.该队列是否只供一个消费者进行消费共享，true为可以多个消费者消费，false只能一个消费者消费
             * 4.是否自动删除，最后一个消费者断开连接后该队列是否执行自动删除-true自动删除，false不自动删除
             * 5.其他参数
             */
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String message = "Hello Rabbit MQ";
            /**
             * 进行消息发送
             * 1.目标交换机
             * 2.队列名称（routingKey）
             * 3.其他参数信息
             * 4.发送消息的消息体
             */
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("[X] Sent '" + message + "'");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        } catch (TimeoutException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }
}
