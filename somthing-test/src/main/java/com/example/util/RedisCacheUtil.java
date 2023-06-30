package com.example.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Redis缓存工具类
 */
@Component
public class RedisCacheUtil {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 存储基本对象，String、int、Object等
     * @param key
     * @param object
     * @param <T>
     */
    public <T> void setCacheObject(final String key, final T object) {
        redisTemplate.opsForValue().set(key,  object);
    }

    /**
     * 存储基本对象，String、int、Object等
     * @param key
     * @param object
     * @param <T>
     */
    public <T> void setCacheObjectToString(final String key, final T object) {
        stringRedisTemplate.opsForValue().set(key,  object.toString());
    }
}
