package com.example;

import com.example.topic.ProducerTopic;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
public class TopicTests {
    @Resource
    private ProducerTopic producerTopic;

    @Test
    void testRule1() {
        producerTopic.produce("topic.color.msg.msg", "生产者生产消息");
    }
}
