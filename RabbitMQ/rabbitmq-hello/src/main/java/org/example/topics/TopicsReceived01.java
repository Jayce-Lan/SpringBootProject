package org.example.topics;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.example.util.RabbitMQConfigDiction;
import org.example.util.RabbitMQTestUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * Topics - 主题交换模式的消费者
 */
public class TopicsReceived01 {
    /**
     * 规则名称：TOPICS_RULE_01_ONLY_ORANGE
     */
    public static final String TOPICS_RULE_01_ONLY_ORANGE = "*.orange.*";

    public static void main(String[] args) throws IOException, TimeoutException {
        TopicsReceived01 topicsReceived01 = new TopicsReceived01();
        topicsReceived01.testTopicsReceived();
    }

    private void testTopicsReceived() throws IOException, TimeoutException {
        Channel channel = RabbitMQTestUtils.getChannel();
        String queueName = channel.queueDeclare().getQueue();
        channel.exchangeDeclare(RabbitMQConfigDiction.TOPICS_EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        /**
         * 绑定队列、交换机、规则
         * 1.队列名
         * 2.交换机
         * 3.规则
         */
        channel.queueBind(queueName, RabbitMQConfigDiction.TOPICS_EXCHANGE_NAME, TOPICS_RULE_01_ONLY_ORANGE);
        System.out.println("Received01 Wait for received and log the message...");
        channel.basicConsume(queueName, true,
                RabbitMQConfigDiction.TOPICS_RECEIVED_SUCCESS_CALL_BACK,
                RabbitMQConfigDiction.RECEIVED_CANCEL_CALL_BACK);
    }
}
