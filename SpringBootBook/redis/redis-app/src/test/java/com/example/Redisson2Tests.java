package com.example;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * 可重入锁的实现跟踪
 */

@SpringBootTest
@Slf4j
public class Redisson2Tests {
    @Resource
    private RedissonClient redissonClient;

    private RLock lock;

    @BeforeEach
    void setUp() {
        lock = redissonClient.getLock("testRlock");
    }

    @Test
    void method1() {
        boolean lockFlag = lock.tryLock();
        if (!lockFlag) {
            log.info("获取锁失败。。。。。1");
            return;
        }
        try {
            log.info("获取锁成功。。。。。1");
            method2();
            log.info("开始执行业务。。。。。1");
        } finally {
            log.info("准备释放锁。。。。。1");
            lock.unlock();
        }
    }

    /**
     * 执行method2时，重入锁的value会变为2，直至释放
     */
    void method2() {
        boolean lockFlag = lock.tryLock();
        if (!lockFlag) {
            log.info("获取锁失败。。。。。2");
            return;
        }
        try {
            log.info("获取锁成功。。。。。2");
            log.info("开始执行业务。。。。。2");
        } finally {
            log.info("准备释放锁。。。。。2");
            lock.unlock();
        }
    }
}
