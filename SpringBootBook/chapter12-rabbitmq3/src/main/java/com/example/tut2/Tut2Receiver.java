package com.example.tut2;

import com.example.myconst.RabbitMQConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.util.StopWatch;

@RabbitListener(queues = RabbitMQConst.WORK_QUEUE)
@Slf4j
public class Tut2Receiver {
    private final int instance;

    public Tut2Receiver(int instance) {
        this.instance = instance;
    }

    @RabbitHandler
    public void receive(String in) throws InterruptedException {
        StopWatch watch = new StopWatch();
        watch.start();
        log.info("instance {} [x] Received '{}'", this.instance, in);
        doWork(in);
        watch.stop();
        log.info("instance {} [x] Done in {} s", this.instance, watch.getTotalTimeSeconds());
    }

    public void doWork(String in) throws InterruptedException {
        for (char ch : in.toCharArray()) {
            if (ch == '.') {
                Thread.sleep(1000);
            }
        }
    }
}
