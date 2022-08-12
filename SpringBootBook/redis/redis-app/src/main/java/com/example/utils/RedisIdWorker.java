package com.example.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * 基于Redis实现的全局id生成器
 * 为了让id更加安全，不直接使用自增数值，而是采取一些拼接
 * 生成id的策略：
 * 1bit为符号位，永远为0
 * 31bit以秒为单位，可以使用69年
 * 32bit，秒内的计数器，也就是redis的计数器，支持生产2^32个不同的id
 */
@Component
public class RedisIdWorker {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 以 2022年1月1日作为开始时间戳
     */
    private static final Long BEGIN_TIMESTAMP = 1640995200L;
    /**
     * 位运算常量
     */
    private static final int COUNT_BITS = 32;

    /**
     * 生成全局id
     * @param keyPrefix id名称
     * @return 返回id结果
     */
    public long nextId(String keyPrefix) {
        // 生成时间戳(当前时间)
        LocalDateTime now = LocalDateTime.now();
        long nowSecond = now.toEpochSecond(ZoneOffset.UTC);
        long timestamp = nowSecond - BEGIN_TIMESTAMP;

        // 生成序列号
        //获取当前日期
        String yyyyMMdd = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        // 后缀添加当前日期后，使得Redis不会超出自增的上限
        long count = stringRedisTemplate.opsForValue().increment(RedisConstants.INCR_ID_KEY + keyPrefix + ":" + yyyyMMdd);

        // 拼接并返回
        return timestamp << COUNT_BITS | count;
    }
}
