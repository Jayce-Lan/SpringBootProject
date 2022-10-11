package com.example.utils;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

public class SimpleRedisLock implements ILock {
    private StringRedisTemplate stringRedisTemplate;
    /**
     * 锁的名称，即为业务名称
     */
    private String lockName;
    /**
     * 锁的前缀，将锁的主键统一归类
     */
    private static final String KEY_PREFIX = "lock:";

    public SimpleRedisLock(StringRedisTemplate stringRedisTemplate, String lockName) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.lockName = lockName;
    }

    @Override
    public boolean tryLock(long timeoutSec) {
        // 获取线程标识
        long threadId = Thread.currentThread().getId();
        // 获取锁，如果锁不存在才执行操作
        Boolean tryLockFlag = this.stringRedisTemplate.opsForValue().setIfAbsent(KEY_PREFIX + lockName, threadId + "", timeoutSec, TimeUnit.SECONDS);
        // 防止出现空指针
        return Boolean.TRUE.equals(tryLockFlag);
    }

    @Override
    public void unLock() {
        stringRedisTemplate.delete(KEY_PREFIX + lockName);
    }
}
