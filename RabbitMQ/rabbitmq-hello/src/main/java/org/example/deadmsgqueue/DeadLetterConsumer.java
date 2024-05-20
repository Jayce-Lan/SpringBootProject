package org.example.deadmsgqueue;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import org.example.util.RabbitMQConfigDiction;
import org.example.util.RabbitMQTestUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 死信队列
 * DeadLetterConsumer - 死信的消费者
 */
public class DeadLetterConsumer {
    /**
     * 死信队列Routing Key
     */
    public static final String DEAD_ROUTING_KEY = "dead";

    public static void main(String[] args) throws IOException, TimeoutException {
        DeadLetterConsumer deadLetterConsumer = new DeadLetterConsumer();
        deadLetterConsumer.testDeadLetterReceived();
    }

    /**
     * 正常消费方法
     * 正常消费失败后，由正常交换机转发给死信交换机，之后由后者对应消费者进行消费
     * 而不是直接由生产者直接发送给死信交换机
     */
    private void testDeadLetterReceived() throws IOException, TimeoutException {
        Channel channel = RabbitMQTestUtils.getChannel();
        // 死信队列声明
        channel.queueDeclare(RabbitMQConfigDiction.DEAD_QUEUE, false, false, false, null);
        // 声明交换机，类型为direct
        channel.exchangeDeclare(RabbitMQConfigDiction.DEAD_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        // 关系绑定
        channel.queueBind(RabbitMQConfigDiction.DEAD_QUEUE, RabbitMQConfigDiction.DEAD_EXCHANGE_NAME, DEAD_ROUTING_KEY);
        // 交换机与队列绑定
        System.out.println("DeadLetterConsumer is Received ...");
        channel.basicConsume(RabbitMQConfigDiction.DEAD_QUEUE, true,
                RabbitMQConfigDiction.TOPICS_RECEIVED_SUCCESS_CALL_BACK,
                RabbitMQConfigDiction.RECEIVED_CANCEL_CALL_BACK);
    }
}
