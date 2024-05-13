package org.example.fanout;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import org.example.util.RabbitMQTestUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.example.util.RabbitMQConfigDiction.*;

/**
 * Fanout扇出模式的接收方1
 */
public class Received01 {
    public static void main(String[] args) throws IOException, TimeoutException {
        Received01 received01 = new Received01();
        received01.testFanoutReceived();
    }

    private void testFanoutReceived() throws IOException, TimeoutException {
        Channel channel = RabbitMQTestUtils.getChannel();
        /**
         * 声明交换机
         * 1.交换机名称
         * 2.交换机类型
         */
        channel.exchangeDeclare(FANOUT_EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        /**
         * 获取临时队列，队列名称随即
         * 当消费者断开连接，队列自动删除
         */
        String queueName = channel.queueDeclare().getQueue();
        /**
         * 绑定交换机与信道
         * 1.队列名
         * 2.交换机名称
         * 3.RoutingKey-为空则为Fanout广播模式
         */
        channel.queueBind(queueName, FANOUT_EXCHANGE_NAME, "");
        System.out.println("Received01 Wait for received and log the message...");
        channel.basicConsume(queueName, true, RECEIVED_SUCCESS_CALL_BACK, RECEIVED_CANCEL_CALL_BACK);
    }
}
