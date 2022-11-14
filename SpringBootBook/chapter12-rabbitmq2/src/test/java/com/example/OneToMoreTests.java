package com.example;

import com.example.onetomore.ProducerOneToMore;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * 工作队列（一个生产者对多个消费者）模式测试类
 */
@SpringBootTest
@Slf4j
public class OneToMoreTests {
    @Resource
    ProducerOneToMore producerOneToMore;

    @Test
    void testOneToMore() throws InterruptedException {
        producerOneToMore.produce();
    }
}
