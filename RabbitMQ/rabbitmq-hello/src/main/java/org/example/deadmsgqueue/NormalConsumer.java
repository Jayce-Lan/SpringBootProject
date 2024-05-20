package org.example.deadmsgqueue;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.example.util.RabbitMQConfigDiction;
import org.example.util.RabbitMQTestUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 死信队列
 * NormalConsumer - 正常的消费者
 */
public class NormalConsumer {
    /**
     * 正常队列Routing Key
     */
    public static final String NORMAL_ROUTING_KEY = "normal";
    /**
     * 死信队列Routing Key
     */
    public static final String DEAD_ROUTING_KEY = "dead";

    public static void main(String[] args) throws IOException, TimeoutException {
        NormalConsumer normalConsumer = new NormalConsumer();
        // 模拟TTL超时和队列最大长度
//        normalConsumer.testNormalReceived();
        // 模拟消息拒收
        normalConsumer.testMsgDontRecReceived();
    }

    /**
     * 正常消费方法
     * 正常消费失败后，由正常交换机转发给死信交换机，之后由后者对应消费者进行消费
     * 而不是直接由生产者直接发送给死信交换机
     */
    private void testNormalReceived() throws IOException, TimeoutException {
        Channel channel = RabbitMQTestUtils.getChannel();
        // 独自生成队列与交换机，用作正常消费的队列与交换机
        /**
         * 正常队列声明
         * 在此处，正常的交换机声明需要用到最后的map参数指定消息成为死信后要转发至死信交换机
         * 队列声明中的arguments用于承接转发规则等信息
         */
        Map<String, Object> arguments = new HashMap<>();
        // 正常队列声明设置死信交换机
        arguments.put("x-dead-letter-exchange", RabbitMQConfigDiction.DEAD_EXCHANGE_NAME);
        // 设置死信RoutingKey
        arguments.put("x-dead-letter-routing-key", DEAD_ROUTING_KEY);
        // 设置队列最大长度 - 一旦超过此长度则会交由死信队列消费
        arguments.put("x-max-length", 6);
        // 过期时间 - 10s = 10000ms ，也可以在生产者进行声明
//        arguments.put("x-message-ttl", 10000);
        channel.queueDeclare(RabbitMQConfigDiction.NORMAL_QUEUE, false, false, false, arguments);
        // 死信队列声明
        channel.queueDeclare(RabbitMQConfigDiction.DEAD_QUEUE, false, false, false, null);
        // 声明交换机，类型为direct
        channel.exchangeDeclare(RabbitMQConfigDiction.NORMAL_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(RabbitMQConfigDiction.DEAD_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        // 关系绑定
        channel.queueBind(RabbitMQConfigDiction.NORMAL_QUEUE, RabbitMQConfigDiction.NORMAL_EXCHANGE_NAME, NORMAL_ROUTING_KEY);
        channel.queueBind(RabbitMQConfigDiction.DEAD_QUEUE, RabbitMQConfigDiction.DEAD_EXCHANGE_NAME, DEAD_ROUTING_KEY);
        // 交换机与队列绑定
        System.out.println("Normal is Received ...");
        channel.basicConsume(RabbitMQConfigDiction.NORMAL_QUEUE, true,
                RabbitMQConfigDiction.TOPICS_RECEIVED_SUCCESS_CALL_BACK,
                RabbitMQConfigDiction.RECEIVED_CANCEL_CALL_BACK);
    }

    /**
     * 正常消费方法 - 模拟消息拒收
     */
    private void testMsgDontRecReceived() throws IOException, TimeoutException {
        Channel channel = RabbitMQTestUtils.getChannel();
        // 独自生成队列与交换机，用作正常消费的队列与交换机
        /**
         * 正常队列声明
         * 在此处，正常的交换机声明需要用到最后的map参数指定消息成为死信后要转发至死信交换机
         * 队列声明中的arguments用于承接转发规则等信息
         */
        Map<String, Object> arguments = new HashMap<>();
        // 正常队列声明设置死信交换机
        arguments.put("x-dead-letter-exchange", RabbitMQConfigDiction.DEAD_EXCHANGE_NAME);
        // 设置死信RoutingKey
        arguments.put("x-dead-letter-routing-key", DEAD_ROUTING_KEY);
        channel.queueDeclare(RabbitMQConfigDiction.NORMAL_QUEUE, false, false, false, arguments);
        // 死信队列声明
        channel.queueDeclare(RabbitMQConfigDiction.DEAD_QUEUE, false, false, false, null);
        // 声明交换机，类型为direct
        channel.exchangeDeclare(RabbitMQConfigDiction.NORMAL_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(RabbitMQConfigDiction.DEAD_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        // 关系绑定
        channel.queueBind(RabbitMQConfigDiction.NORMAL_QUEUE, RabbitMQConfigDiction.NORMAL_EXCHANGE_NAME, NORMAL_ROUTING_KEY);
        channel.queueBind(RabbitMQConfigDiction.DEAD_QUEUE, RabbitMQConfigDiction.DEAD_EXCHANGE_NAME, DEAD_ROUTING_KEY);
        // 交换机与队列绑定
        System.out.println("Normal is Received ...");
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String msg = new String(message.getBody(), StandardCharsets.UTF_8);
            if (msg.contains("5")) {
                System.out.println("[!] This message is dont received, and sent to dead exchange: [" + msg + "]");
                /**
                 * 声明队列拒收
                 * 1.队列标签
                 * 2.是否返回队列 - 指当前正常normal队列，而不是死信队列
                 */
                channel.basicReject(message.getEnvelope().getDeliveryTag(), false);
            } else {
                System.out.println("[X] Received ... The message is [" + msg
                        + "] And the RoutingKey type is [" + message.getEnvelope().getRoutingKey() + "]");
                channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
            }
        };
        // 需要开启手动应答
        channel.basicConsume(RabbitMQConfigDiction.NORMAL_QUEUE, false,
                deliverCallback,
                RabbitMQConfigDiction.RECEIVED_CANCEL_CALL_BACK);
    }
}
