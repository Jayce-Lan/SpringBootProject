package com.example.tut1;

import com.example.myconst.RabbitMQConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@RabbitListener(queues = RabbitMQConst.HELLO_QUEUE)
@Slf4j
public class Tut1Receiver {
    @RabbitHandler
    public void receiver(String in) {
        log.info("[x] Received '{}'", in);
    }
}
