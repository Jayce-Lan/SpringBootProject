package org.example.workqueues;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import org.example.util.RabbitMQConfigDiction;
import org.example.util.RabbitMQTestUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * 生产者代码
 */
public class Task01 {
    public static void main(String[] args) {
        Task01 task01 = new Task01();
        // 简单的工作队列模式
//        task01.testWorkQueueSent();
        // 手动应答
//        task01.testWorkQueueAckSent();
        // 队列持久化
        task01.testWorkQueueDurable();
    }

    /**
     * 简单的工作队列发送方
     */
    private void testWorkQueueSent() {
        try (Channel channel = RabbitMQTestUtils.getChannel()) {
            // 声明队列
            channel.queueDeclare(RabbitMQConfigDiction.TASK_QUEUE, false, false, false, null);
            // 从控制台接收信息
            System.out.println("please input the message");
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String message = scanner.next();
                channel.basicPublish("", RabbitMQConfigDiction.TASK_QUEUE, null, message.getBytes(StandardCharsets.UTF_8));
                System.out.println("be sent successfully! the message is : [" + message + "]");
            }
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    /**
     * 消息手动应答发送方
     * 本次主要测试消费者，因此生产者代码与上一致，只是使用了不同的消息队列名称
     */
    private void testWorkQueueAckSent() {
        try (Channel channel = RabbitMQTestUtils.getChannel()) {
            // 声明队列
            channel.queueDeclare(RabbitMQConfigDiction.TASK_ACK_QUEUE, false, false, false, null);
            // 从控制台接收信息
            System.out.println("please input the message");
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String message = scanner.next();
                channel.basicPublish("", RabbitMQConfigDiction.TASK_ACK_QUEUE, null, message.getBytes(StandardCharsets.UTF_8));
                System.out.println("be sent successfully! the message is : [" + message + "]");
            }
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    /**
     * 队列持久化
     * 消息持久化
     */
    private void testWorkQueueDurable() {
        try (Channel channel = RabbitMQTestUtils.getChannel();) {
            // 持久化队列Queue的参数-durable
            boolean durable = true;
            channel.queueDeclare(RabbitMQConfigDiction.TASK_DURABLE_QUEUE, durable, false, false, null);
            // 从控制台接收信息
            System.out.println("please input the message");
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String message = scanner.next();
                channel.basicPublish("", RabbitMQConfigDiction.TASK_DURABLE_QUEUE,
                        MessageProperties.PERSISTENT_TEXT_PLAIN, // 消息持久化
                        message.getBytes(StandardCharsets.UTF_8));
                System.out.println("be sent successfully! the message is : [" + message + "]");
            }
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
