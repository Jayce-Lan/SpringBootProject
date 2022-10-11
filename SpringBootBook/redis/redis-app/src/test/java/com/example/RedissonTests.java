package com.example;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@Slf4j
public class RedissonTests {
    @Resource
    private RedissonClient redissonClient;

    /**
     * 基于 redisson 实现分布式锁
     * @throws InterruptedException
     */
    @Test
    void testRedisson() throws InterruptedException {
        // 获取锁（可重入），指定锁的名称
        RLock rLock = redissonClient.getLock("testLock");
        // 尝试获取锁，参数分别为：获取锁的最大等待时间（期间会重试），锁自动释放时间，时间单位
        boolean lockFlag = rLock.tryLock(1, 10, TimeUnit.SECONDS);
        if (lockFlag) {
            try {
                log.info("执行业务");
            } finally {
                // 释放锁
                rLock.unlock();
            }
        }
    }
}
