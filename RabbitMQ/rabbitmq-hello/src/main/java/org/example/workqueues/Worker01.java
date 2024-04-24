package org.example.workqueues;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.example.util.RabbitMQConfigDiction;
import org.example.util.RabbitMQTestUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 工作线程（消费者）
 * 主要用于轮询消费生产者的大量信息
 */
public class Worker01 {
    public static void main(String[] args) {
        Worker01 worker01 = new Worker01();
        try {
            worker01.testWorkQueuesReceived();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    /**
     * 简单的工作队列模式接收方
     * @throws IOException
     * @throws TimeoutException
     */
    private void testWorkQueuesReceived() throws IOException, TimeoutException {
        Channel channel = RabbitMQTestUtils.getChannel();

        // 声明队列-消费者如果不声明队列，那么如果首次创建对象，生产者未声明队列前消费者就启动，会触发NOT_FOUND异常
        channel.queueDeclare(RabbitMQConfigDiction.TASK_QUEUE, false, false, false, null);
        // 额外的 DeliverCallback 接口，接收消息成功时执行。
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("[X] Received '" + message + "'");
        };
        // 消息接收取消后的接口
        CancelCallback cancelCallback = consumerTag -> System.out.println(consumerTag + "Received is cancel!");
        System.out.println("Work01 is waiting");
        // 消息接收
        channel.basicConsume(RabbitMQConfigDiction.TASK_QUEUE, true, deliverCallback, cancelCallback);
    }
}
