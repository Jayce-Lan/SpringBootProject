package com.example.chapter11cache;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 使用 StringRedisTemplate 操作Redis
 * 1. 两者的关系是StringRedisTemplate继承RedisTemplate。
 *
 * 2. 两者的数据是不共通的；也就是说StringRedisTemplate只能管理StringRedisTemplate里面的数据，RedisTemplate只能管理RedisTemplate中的数据。
 *
 * 3. SDR默认采用的序列化策略有两种，一种是String的序列化策略，一种是JDK的序列化策略。
 *
 * StringRedisTemplate默认采用的是String的序列化策略，保存的key和value都是采用此策略序列化保存的。
 *
 * RedisTemplate默认采用的是JDK的序列化策略，保存的key和value都是采用此策略序列化保存的。
 *
 * RedisTemplate默认使用的序列类在在操作数据的时候，比如说存入数据会将数据先序列化成字节数组然后在存入Redis数据库，
 * 这个时候打开Redis查看的时候，你会看到你的数据不是以可读的形式展现的，而是以字节数组显示
 *
 * 当然从Redis获取数据的时候也会默认将数据当做字节数组转化，这都是根据序列化策略来决定的。
 *
 * 而stringredistemplate，默认存入的数据就是原文，因为stringRedistemplate默认使用的是string序列化策略，使用stringredistemplate默认存入数据，
 * 数据在Redis查看工具是正常写入的状态，写入时是什么，那么查看时就是什么
 */

@SpringBootTest
@Slf4j
public class Chapter11StringRedisTemplateApplicationTests {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void optionString() {
        try {
            stringRedisTemplate.opsForValue().set("S_NUM", "123");
            stringRedisTemplate.opsForValue().set("S_NUM_LONG_TIME", "XYZ", 3, TimeUnit.SECONDS);
            log.info("S_NUM: {}", stringRedisTemplate.opsForValue().get("S_NUM"));
            log.info("S_NUM_LONG_TIME: {}", stringRedisTemplate.opsForValue().get("S_NUM_LONG_TIME"));
            Thread.sleep(2000);
            log.info("S_NUM_LONG_TIME: {}", stringRedisTemplate.opsForValue().get("S_NUM_LONG_TIME"));
            Thread.sleep(2000);
            log.info("S_NUM_LONG_TIME: {}", stringRedisTemplate.opsForValue().get("S_NUM_LONG_TIME"));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void optionHash() {
        Map<String, Object> tempMap = new HashMap<>();
        tempMap.put("name", "Jayce");
        tempMap.put("sex", "男");
        stringRedisTemplate.opsForHash().putAll("S_USER", tempMap);
        log.info("S_USER: {}", stringRedisTemplate.opsForHash().entries("S_USER"));
    }

    @Test
    void optionList() {
        String[] strings = new String[]{"1", "2", "3"};
        stringRedisTemplate.opsForList().leftPushAll("S_LIST", strings);
        stringRedisTemplate.opsForList().leftPush("S_LIST", "4");
        log.info("S_LIST: {}", stringRedisTemplate.opsForList().range("S_LIST", 0, -1));
    }

    @Test
    void addValueToList() {
        for (int i = 0; i <= 30000; i++) {
            stringRedisTemplate.opsForList().rightPush("listkey", String.valueOf(i));
        }
    }
}
