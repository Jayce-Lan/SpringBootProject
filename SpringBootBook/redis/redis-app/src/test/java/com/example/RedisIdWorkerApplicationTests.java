package com.example;

import com.example.utils.RedisIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
@Slf4j
public class RedisIdWorkerApplicationTests {
    @Resource
    RedisIdWorker redisIdWorker;

    /**
     * 线程池
     */
    private ExecutorService executorService = Executors.newFixedThreadPool(500);

    @Test
    void testTime() {
        LocalDateTime localDateTime = LocalDateTime.of(2022, 8, 13, 0, 0 ,0);
        long second = localDateTime.toEpochSecond(ZoneOffset.UTC);
        log.info("second: {}", second);
    }

    @Test
    void testIdWorker() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(300);
        Runnable task = () -> {
            for (int i = 0; i < 100; i++) {
                long order = redisIdWorker.nextId("order");
                log.info("order: {}", order);
            }
            latch.countDown();
        };
        long begin = System.currentTimeMillis();
        // 将任务提交300次
        for (int i = 0; i < 300; i++) {
            executorService.submit(task);
        }
        // 等待线程执行结束
        latch.await();
        long end = System.currentTimeMillis();
        log.info("使用时间： {}", (end - begin));
    }
}
