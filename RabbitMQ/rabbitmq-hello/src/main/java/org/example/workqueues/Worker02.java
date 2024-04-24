package org.example.workqueues;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.example.util.RabbitMQConfigDiction;
import org.example.util.RabbitMQTestUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 用于测试消费者手动应答消费消息的02消费者，与01消费者有别，因此独立创建
 */
public class Worker02 {
    public static void main(String[] args) {
        Worker02 worker02 = new Worker02();
        try {
            worker02.testWorkQueuesAckReceived02();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    /**
     * 手动应答时不丢失，放回队列重新消费
     * @throws IOException
     * @throws TimeoutException
     */
    private void testWorkQueuesAckReceived02() throws IOException, TimeoutException {
        final Channel channel = RabbitMQTestUtils.getChannel();
        channel.queueDeclare(RabbitMQConfigDiction.TASK_ACK_QUEUE, false, false, false, null);
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            // 模拟事务处理，本例为较慢，因此沉睡30秒
            RabbitMQTestUtils.getSleep(30);
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
        System.out.println("Work02 is waiting...slow");
        // 采用手动应答
        boolean autoAck = false;
        channel.basicConsume(RabbitMQConfigDiction.TASK_ACK_QUEUE, autoAck, deliverCallback, cancelCallback);
    }
}
