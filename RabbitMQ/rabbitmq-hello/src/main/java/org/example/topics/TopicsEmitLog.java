package org.example.topics;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import org.example.util.RabbitMQConfigDiction;
import org.example.util.RabbitMQTestUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

/**
 * Topics - 主题模式生产者
 */
public class TopicsEmitLog {
    public static void main(String[] args) {
        TopicsEmitLog topicsEmitLog = new TopicsEmitLog();
        topicsEmitLog.testTopicsSent();
    }

    private void testTopicsSent() {
        try (Channel channel = RabbitMQTestUtils.getChannel()) {
            channel.exchangeDeclare(RabbitMQConfigDiction.TOPICS_EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
            Map<String, String> bindingKeyMap = new HashMap<>();
            bindingKeyMap.put("quick.orange.rabbit", "R1&&R2");
            bindingKeyMap.put("lazy.orange.elephant", "R1&R2");
            bindingKeyMap.put("quick.orange.fox", "R1");
            bindingKeyMap.put("lazy.brown.fox", "R2");
            bindingKeyMap.put("lazy.pink.rabbit", "R2");
            bindingKeyMap.put("quick.brown.fox", "NULL");
            bindingKeyMap.put("quick.orange.male.rabbit", "NULL");
            bindingKeyMap.put("lazy.orange.male.rabbit", "R2");
            bindingKeyMap.forEach((routingKey, message) -> {
                System.out.println("message: [" + message + "], routingKey: [" + routingKey + "] is sent success!");
                try {
                    channel.basicPublish(RabbitMQConfigDiction.TOPICS_EXCHANGE_NAME, routingKey,
                            null, message.getBytes(StandardCharsets.UTF_8));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
