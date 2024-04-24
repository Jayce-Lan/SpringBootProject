package org.example.workqueues;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.example.util.RabbitMQConfigDiction;
import org.example.util.RabbitMQTestUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * 工作线程（消费者）
 * 主要用于轮询消费生产者的大量信息
 */
public class Worker01 {
    public static void main(String[] args) {
        Worker01 worker01 = new Worker01();
        try {
            // 简单的工作队列模式
//            worker01.testWorkQueuesReceived();
            // 手动应答
//            worker01.testWorkQueuesAckReceived01();
            // 公平调度
            worker01.testWorkQueuesFairDispatchReceived01();
        } catch (IOException | TimeoutException e) {
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

    /**
     * 手动应答时不丢失，放回队列重新消费
     * @throws IOException
     * @throws TimeoutException
     */
    private void testWorkQueuesAckReceived01() throws IOException, TimeoutException {
        final Channel channel = RabbitMQTestUtils.getChannel();
        channel.queueDeclare(RabbitMQConfigDiction.TASK_ACK_QUEUE, false, false, false, null);
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            // 模拟事务处理，本例为较快，因此沉睡1秒
            RabbitMQTestUtils.getSleep(1);
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("[X] Received '" + message + "'");
            /**
             * 手动应答
             * 1.消息的标记，long类型参数（tag）
             * 2.是否批量应答（multiple）
             */
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };
        // 消息接收取消后的接口
        CancelCallback cancelCallback = consumerTag -> System.out.println(consumerTag + "Received is cancel!");
        System.out.println("Work01 is waiting...fast");
        // 采用手动应答
        boolean autoAck = false;
        channel.basicConsume(RabbitMQConfigDiction.TASK_ACK_QUEUE, autoAck, deliverCallback, cancelCallback);
    }

    /**
     * 持久化测试接收方
     * @throws IOException
     * @throws TimeoutException
     */
    private void testWorkQueuesDurableReceived() throws IOException, TimeoutException {
        Channel channel = RabbitMQTestUtils.getChannel();
        // 同样声明持久化队列
        channel.queueDeclare(RabbitMQConfigDiction.TASK_DURABLE_QUEUE, true, false, false, null);
        // 额外的 DeliverCallback 接口，接收消息成功时执行。
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("[X] Received '" + message + "'");
        };
        // 消息接收取消后的接口
        CancelCallback cancelCallback = consumerTag -> System.out.println(consumerTag + "Received is cancel!");
        System.out.println("Work01 is waiting");
        // 消息接收
        channel.basicConsume(RabbitMQConfigDiction.TASK_DURABLE_QUEUE, true, deliverCallback, cancelCallback);
    }

    /**
     * 公平调度
     * @throws IOException
     * @throws TimeoutException
     */
    private void testWorkQueuesFairDispatchReceived01() throws IOException, TimeoutException {
        final Channel channel = RabbitMQTestUtils.getChannel();
        channel.queueDeclare(RabbitMQConfigDiction.TASK_DURABLE_QUEUE, true, false, false, null);
        // 公平调度
        channel.basicQos(1);
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            // 模拟事务处理，本例为较慢，因此沉睡1秒
            RabbitMQTestUtils.getSleep(2);
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("[X] Received '" + message + "'");
            /**
             * 手动应答
             * 1.消息的标记，long类型参数（tag）
             * 2.是否批量应答（multiple）
             */
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };
        // 消息接收取消后的接口
        CancelCallback cancelCallback = consumerTag -> System.out.println(consumerTag + "Received is cancel!");
        System.out.println("Work01 is waiting...fast");
        // 采用手动应答
        boolean autoAck = false;
        channel.basicConsume(RabbitMQConfigDiction.TASK_DURABLE_QUEUE, autoAck, deliverCallback, cancelCallback);
    }
}
