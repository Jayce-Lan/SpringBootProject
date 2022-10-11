package com.example.utils;

import cn.hutool.core.lang.UUID;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
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
    /**
     * 线程锁的标识，防止极端情况发生时，其他线程删除了当前线程的锁
     */
    private static final String ID_PREFIX = UUID.randomUUID().toString(true) + "-";
    /**
     * Redis 的lua脚本做一个提前的加载
     */
    private static final DefaultRedisScript<Long> UNLOCK_SCRIPT;
    // 静态代码块中的静态变量在类加载时就会加载完成
    static {
        UNLOCK_SCRIPT = new DefaultRedisScript<>();
        // 设置脚本位置
        UNLOCK_SCRIPT.setLocation(new ClassPathResource("unlock.lua"));
        // 设置返回值
        UNLOCK_SCRIPT.setResultType(Long.class);
    }

    public SimpleRedisLock(StringRedisTemplate stringRedisTemplate, String lockName) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.lockName = lockName;
    }

    @Override
    public boolean tryLock(long timeoutSec) {
        // 获取线程标识
        String threadId = ID_PREFIX +  Thread.currentThread().getId();
        // 获取锁，如果锁不存在才执行操，类似于 Redis 中的 set nx，并且同时执行保证事务的原子性
        Boolean tryLockFlag = this.stringRedisTemplate.opsForValue().setIfAbsent(KEY_PREFIX + lockName, threadId, timeoutSec, TimeUnit.SECONDS);
        // 防止出现空指针
        return Boolean.TRUE.equals(tryLockFlag);
    }

    /**
     * 这种形式的释放锁会存在事务原子性的问题
     * 由于判断锁与释放锁并不在同时执行
     * jvm回收释放线程时，会有极端情况导致锁的错误释放
     */
//    @Override
//    public void unLock() {
//        // 获取线程标识
//        String threadId = ID_PREFIX +  Thread.currentThread().getId();
//        // 获取锁中的标识
//        String id = stringRedisTemplate.opsForValue().get(KEY_PREFIX + lockName);
//        // 判定线程标识是否一致，防止出现极端情况下锁误删的问题
//        if (threadId.equals(id)) {
//            stringRedisTemplate.delete(KEY_PREFIX + lockName);
//        }
//    }


    /**
     * 使用LUA语言保证事务的原子性
     * Redis 提供了 Lua 脚本功能，能够在一个脚本中写入多个Redis命令
     * Redis.call(命令名称, key, value)
     */
    @Override
    public void unLock() {
        stringRedisTemplate.execute(UNLOCK_SCRIPT,
                Collections.singletonList(KEY_PREFIX + lockName), // KEYS
                ID_PREFIX +  Thread.currentThread().getId()); // ARGV
    }
}
