package org.example.topics;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import org.example.util.RabbitMQConfigDiction;
import org.example.util.RabbitMQTestUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Topics - 主题交换模式的消费者
 */
public class TopicsReceived02 {
    /**
     * 规则名称：TOPICS_RULE_02_ONLY_RABBIT
     */
    public static final String TOPICS_RULE_02_ONLY_RABBIT = "*.*.rabbit";
    /**
     * 规则名称： TOPICS_RULE_03_LAZY_ALL
     */
    public static final String TOPICS_RULE_03_LAZY_ALL = "lazy.#";

    public static void main(String[] args) throws IOException, TimeoutException {
        TopicsReceived02 topicsReceived02 = new TopicsReceived02();
        topicsReceived02.testTopicsReceived();
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
        channel.queueBind(queueName, RabbitMQConfigDiction.TOPICS_EXCHANGE_NAME, TOPICS_RULE_02_ONLY_RABBIT);
        channel.queueBind(queueName, RabbitMQConfigDiction.TOPICS_EXCHANGE_NAME, TOPICS_RULE_03_LAZY_ALL); // 绑定两个规则
        System.out.println("Received02 Wait for received and log the message...");
        channel.basicConsume(queueName, true,
                RabbitMQConfigDiction.TOPICS_RECEIVED_SUCCESS_CALL_BACK,
                RabbitMQConfigDiction.RECEIVED_CANCEL_CALL_BACK);
    }
}
