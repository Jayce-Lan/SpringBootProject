package org.example.deadmsgqueue;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.example.util.RabbitMQConfigDiction;
import org.example.util.RabbitMQTestUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * 死信队列-生产者
 */
public class DeadLetterProducer {
    /**
     * 正常队列Routing Key
     */
    public static final String NORMAL_ROUTING_KEY = "normal";

    public static void main(String[] args) {
        DeadLetterProducer deadLetterProducer = new DeadLetterProducer();
        deadLetterProducer.testMaxLengthSent();
    }

    /**
     * 生产者 - 模拟TTL超时
     * 生产者只需要正常发送消息到正常交换机即可
     * 因为死信转发是由正常交换机来完成的
     */
    private void testMaxTTLSent() {
        try (Channel channel = RabbitMQTestUtils.getChannel()) {
            // 设置死信参数 TTL时间
            AMQP.BasicProperties props = new AMQP.BasicProperties()
                    .builder()
                    .expiration("10000") // 过期时间，ms
                    .build();
            for (int i = 0; i < 10; i++) {
                String message = "info " + i;
                System.out.println("Producer sent message is : [" + message + "]");
                channel.basicPublish(RabbitMQConfigDiction.NORMAL_EXCHANGE_NAME, NORMAL_ROUTING_KEY, props,
                        message.getBytes(StandardCharsets.UTF_8));
            }
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生产者 - 模拟队列达到最大长度
     * 生产者只需要正常发送消息到正常交换机即可
     * 因为死信转发是由正常交换机来完成的
     */
    private void testMaxLengthSent() {
        try (Channel channel = RabbitMQTestUtils.getChannel()) {
            for (int i = 0; i < 10; i++) {
                String message = "info " + i;
                System.out.println("Producer sent message is : [" + message + "]");
                channel.basicPublish(RabbitMQConfigDiction.NORMAL_EXCHANGE_NAME, NORMAL_ROUTING_KEY, null,
                        message.getBytes(StandardCharsets.UTF_8));
            }
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
