package org.example.fanout;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import org.example.util.RabbitMQConfigDiction;
import org.example.util.RabbitMQTestUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * 作为Fanout扇出模式的发送方
 */
public class SentLog {
    public static void main(String[] args) {
        SentLog sentLog = new SentLog();
        sentLog.testFanoutSent();
    }

    private void testFanoutSent() {
        try (Channel channel = RabbitMQTestUtils.getChannel()) {
            /**
             * 声明交换机
             * 1.交换机名称
             * 2.交换机类型
             */
            channel.exchangeDeclare(RabbitMQConfigDiction.FANOUT_EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
            System.out.println("Please input the message...");
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String message = scanner.next();
                // 这回是直接发送给交换机，而不是发送给具体的队列，因此队列名为空；
                // 其实在发布订阅模式下，第二个参数也为"RoutingKey"
                channel.basicPublish(RabbitMQConfigDiction.FANOUT_EXCHANGE_NAME, "", null, message.getBytes(StandardCharsets.UTF_8));
                System.out.println("message: [" + message + "] is sent success!");
            }
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
