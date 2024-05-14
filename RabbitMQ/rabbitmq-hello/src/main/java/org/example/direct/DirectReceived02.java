package org.example.direct;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import org.example.util.RabbitMQTestUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.example.util.RabbitMQConfigDiction.*;

public class DirectReceived02 {
    public static void main(String[] args) throws IOException, TimeoutException {
        DirectReceived02 directReceived02 = new DirectReceived02();
        directReceived02.testDirectReceived();
    }

    private void testDirectReceived() throws IOException, TimeoutException {
        Channel channel = RabbitMQTestUtils.getChannel();
        channel.queueDeclare(DIRECT_DISK_QUEUE, false, false, false, null);
        /**
         * 声明交换机
         * 1.交换机名称
         * 2.交换机类型
         */
        channel.exchangeDeclare(DIRECT_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        /**
         * 绑定交换机与信道
         * 1.队列名
         * 2.交换机名称
         * 3.RoutingKey
         */
        channel.queueBind(DIRECT_DISK_QUEUE, DIRECT_EXCHANGE_NAME, "error");
        System.out.println("DirectReceived02 Wait for received and log the message...");
        channel.basicConsume(DIRECT_DISK_QUEUE, true, RECEIVED_SUCCESS_CALL_BACK, RECEIVED_CANCEL_CALL_BACK);
    }
}
