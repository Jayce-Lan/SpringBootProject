package org.example.direct;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import org.example.util.RabbitMQTestUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.example.util.RabbitMQConfigDiction.*;

public class DirectReceived01 {
    public static void main(String[] args) throws IOException, TimeoutException {
        DirectReceived01 directReceived01 = new DirectReceived01();
        directReceived01.testDirectReceived();
    }

    private void testDirectReceived() throws IOException, TimeoutException {
        Channel channel = RabbitMQTestUtils.getChannel();
        channel.queueDeclare(DIRECT_CONSOLE_QUEUE, false, false, false, null);
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
        channel.queueBind(DIRECT_CONSOLE_QUEUE, DIRECT_EXCHANGE_NAME, "info");
        channel.queueBind(DIRECT_CONSOLE_QUEUE, DIRECT_EXCHANGE_NAME, "warning"); // 绑定两个Routing key

        System.out.println("DirectReceived01 Wait for received and log the message...");
        channel.basicConsume(DIRECT_CONSOLE_QUEUE, true, RECEIVED_SUCCESS_CALL_BACK, RECEIVED_CANCEL_CALL_BACK);
    }
}
