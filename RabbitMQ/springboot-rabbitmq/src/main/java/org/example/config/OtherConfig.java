package org.example.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static org.example.util.CommonDiction.PRIORITY_QUEUE_NAME;

/**
 * 关于其他知识点的绑定关系
 */
@Configuration
@Slf4j
public class OtherConfig {
    /**
     * 设置优先级队列
     * x-max-priority 队列优先级参数的 key，其 value 为0-255中任意数字
     * 但是数字越大对计算机性能影响越高，因为需要堆积队列并重新排序
     * 此方法只有在有消息堆积时有效，消息生产一条就消费一条的情况下是无效的
     * @return 优先级队列
     */
    @Bean
    public Queue priorityQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-max-priority", Integer.valueOf(5));
        return QueueBuilder.durable(PRIORITY_QUEUE_NAME)
                .withArguments(arguments)
                .build();
    }
}
