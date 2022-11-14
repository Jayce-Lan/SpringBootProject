package com.example.onetomore;

import com.example.util.RabbitmqConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 工作队列模式 生产者
 */
@Component
@Slf4j
public class ProducerOneToMore {
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 生产多个信息，并发送至队列，等待消费者消费
     */
    public void produce() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            String message = "生产信息[" + i + "]";
            log.info("生产者生产信息 >>>>>>>>>>>>> {}", message);
            rabbitTemplate.convertAndSend(RabbitmqConst.RABBIT_MQ_QUEUE_ONE_TO_MORE, message);
            // 强行sleep，使得消费者及时消费，否则会出现一直生产直至结束后消费的情况
            Thread.sleep(1);
        }
    }
}
