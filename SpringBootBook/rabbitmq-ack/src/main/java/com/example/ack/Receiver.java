package com.example.ack;

import com.example.consts.DictConst;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 消费者
 */
@Slf4j
public class Receiver {
    @RabbitListener(queues = {DictConst.ACK_QUEUE_NAME})
    public void receive(String msg, Channel channel, Message message) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DictConst.TIME_ALL);
        Date date = new Date();
        String time = simpleDateFormat.format(date);
        log.info("接收消息： {}, 当前时间： {}", msg, time);
    }

}
