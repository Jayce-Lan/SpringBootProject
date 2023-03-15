package com.example.tut3;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.util.StopWatch;

/**
 * 发布/订阅模式下的消费者
 */
@Slf4j
public class Tut3Receiver {
    @RabbitListener(queues = "#{autoDeleteQueue1.name}")
    public void receiver1(String in) throws InterruptedException {
        receiver(in, 1);
    }

    @RabbitListener(queues = "#{autoDeleteQueue2.name}")
    public void receiver2(String in) throws InterruptedException {
        receiver(in, 2);
    }

    public void receiver(String in, int receiver) throws InterruptedException {
        StopWatch watch = new StopWatch();
        watch.start();
        log.info("instance : {} [x] Received '{}'", receiver, in);
        doWork(in);
        watch.stop();
        log.info("instance : {} [x] Done in {} s", receiver, watch.getTotalTimeSeconds());
    }

    private void doWork(String in) throws InterruptedException {
        for (char ch : in.toCharArray()) {
            if (ch == '.') {
                Thread.sleep(1000);
            }
        }
    }
}
