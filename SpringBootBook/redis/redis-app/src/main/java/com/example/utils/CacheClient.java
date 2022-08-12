package com.example.utils;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 用于存储缓存的工具类
 */

@Component
@Slf4j
public class CacheClient {
    private final StringRedisTemplate stringRedisTemplate;

    // 线程池
    private static final ExecutorService CACHE_EXECUTOR = Executors.newFixedThreadPool(10);

    /**
     * 基于构造函数注入 StringRedisTemplate
     * @param stringRedisTemplate
     */
    public CacheClient(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 设置键值对
     * @param key 键
     * @param value 值
     * @param time 过期时间
     * @param timeUnit 时间单位
     */
    public void set(String key, Object value, Long time, TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().set(key, JSONObject.toJSONString(value), time, timeUnit);
    }

    /**
     * 设置逻辑过期
     * @param key 键
     * @param value 过期对象
     * @param time 过期时间
     * @param timeUnit 时间单位
     */
    public void setLogicalExpire(String key, Object value, Long time, TimeUnit timeUnit) {
        RedisData redisData = new RedisData();
        redisData.setData(value);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(timeUnit.toSeconds(time)));
        stringRedisTemplate.opsForValue().set(key, JSONObject.toJSONString(redisData));
    }


    /**
     * 使用泛型封装 缓存穿透
     *
     * @param keyPrefix key前缀
     * @param id 使用泛型声明id，保证id可以传入任意类型
     * @param type 返回值的类型
     * @param dbFallBack 调用的查询方法
     * @param time 超时时间
     * @param timeUnit 时间单位
     * @param <R> 返回类型
     * @param <ID> id类型
     * @return 返回查询结果
     */
    public <R, ID> R queryWithPassThrough(String keyPrefix, ID id, Class<R> type,
                                          Function<ID, R> dbFallBack, Long time, TimeUnit timeUnit) {
        String key = keyPrefix + id;
        // 从Redis中查询缓存
        String json = stringRedisTemplate.opsForValue().get(key);
        // 判断是否存在，如果存在，进行反序列化
        if (StrUtil.isNotBlank(json)) {
            return JSONObject.parseObject(json, type);
        }
        // json为空，但是又不是null，那么要防止缓存穿透
        if (json != null) {
            return null;
        }

        // 如果为null，查询数据库
        R r = dbFallBack.apply(id);

        // 如果函数执行结果为null，那么传入空值
        if (r == null) {
            stringRedisTemplate.opsForValue().set(key, "", RedisConstants.CACHE_NULL_TTL, TimeUnit.MINUTES);
            return null;
        }
        // 存在，写入Redis
        this.set(key, r, time, timeUnit);
        return r;
    }

    /**
     * 封装逻辑过期方法
     *
     * @param keyPrefix key前缀
     * @param id 使用泛型声明id，保证id可以传入任意类型
     * @param type 返回值的类型
     * @param dbFallBack 调用的查询方法
     * @param time 超时时间
     * @param timeUnit 时间单位
     * @param <R> 返回类型
     * @param <ID> id类型
     * @return 返回结果
     */
    public <R, ID> R queryWithLogicalExpire(String keyPrefix, ID id, Class<R> type,
                                                Function<ID, R> dbFallBack, Long time, TimeUnit timeUnit) {
        String key = keyPrefix + id;
        // 从Redis中查询商铺缓存
        String json = stringRedisTemplate.opsForValue().get(key);
        // 如果未命中，直接返回
        if (StrUtil.isBlank(json)) {
            return null;
        }
        // 命中，先要把json反序列化为对象
        RedisData redisData = JSONObject.parseObject(json, RedisData.class);
        Object data = redisData.getData();
        R r = JSONObject.parseObject(JSONObject.toJSONString(data), type);
        LocalDateTime expireTime = redisData.getExpireTime();
        // 判断是否过期
        if (expireTime.isAfter(LocalDateTime.now())) {
            // 未过期，直接返回店铺信息
            return r;
        }
        // 过期，需要缓存重建
        // 缓存重建
        // 获取互斥锁
        String lockKey = RedisConstants.LOCK_SHOP_KEY + id;
        // 判断是否获取锁成功
        boolean isLock = tryLock(lockKey);
        if (isLock) {
            // 成功，开启独立线程，实现缓存重建
            CACHE_EXECUTOR.submit(() -> {
                try {
                    // 重建缓存
                    // 查询数据库
                    R apply = dbFallBack.apply(id);
                    // 写入Redis(逻辑过期)
                    this.setLogicalExpire(key, apply, time, timeUnit);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    // 释放锁
                    unLock(lockKey);
                }
            });
        }
        // 返回过期的商铺信息
        return r;
    }

    /**
     * 使用互斥锁来解决缓存击穿的问题
     * 获取锁
     * @param key
     * @return
     */
    private boolean tryLock(String key) {
        // 10s过期，使用setnx的特性进行互斥锁
        Boolean flage = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", RedisConstants.LOCK_SHOP_TTL, TimeUnit.SECONDS);
        return BooleanUtil.isTrue(flage);
    }

    /**
     * 释放锁
     * @param key
     */
    private void unLock(String key) {
        stringRedisTemplate.delete(key);
    }
}
