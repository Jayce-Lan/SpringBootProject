package org.example.direct;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import org.example.util.RabbitMQConfigDiction;
import org.example.util.RabbitMQTestUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * Direct - 直接模式，路由模式
 * 日志发送方，生产者
 */
public class EmitLog {
    public static void main(String[] args) {
        EmitLog emitLog = new EmitLog();
        emitLog.testDirectSent();
    }

    private void testDirectSent() {
        try (Channel channel = RabbitMQTestUtils.getChannel()) {
            String routingKey = "";
            int count = 0;
            /**
             * 声明交换机
             * 1.交换机名称
             * 2.交换机类型
             */
            channel.exchangeDeclare(RabbitMQConfigDiction.DIRECT_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            System.out.println("Please input the message...");
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String message = scanner.next();
                switch (count) {
                    case 0:
                        routingKey = "info";
                        count++;
                        break;
                    case 1:
                        routingKey = "error";
                        count++;
                        break;
                    default:
                        routingKey = "warning";
                        count = 0;
                }
                // 在发布订阅模式下，第二个参数也为"RoutingKey"
                channel.basicPublish(RabbitMQConfigDiction.DIRECT_EXCHANGE_NAME, routingKey, null, message.getBytes(StandardCharsets.UTF_8));
                System.out.println("message: [" + message + "], routingKey: [" + routingKey + "] is sent success!");
            }
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
