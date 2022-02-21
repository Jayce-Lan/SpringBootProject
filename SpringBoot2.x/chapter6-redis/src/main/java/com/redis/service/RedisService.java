package com.redis.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RedisService {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 设置redis的键值对
     * @param key 键
     * @param value 与键对应的值
     */
    public void set(String key, Object value) {
        // 在redis查看编码问题
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // redis查看值的编码问题
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
        ValueOperations<String, Object> vo = redisTemplate.opsForValue();
        vo.set(key, value);
    }

    /**
     * 获取redis的键值对
     * @param key 键
     * @return 根据键获取值
     */
    public Object get(String key) {
        ValueOperations<String, Object> vo = redisTemplate.opsForValue();
        return vo.get(key);
    }
}
