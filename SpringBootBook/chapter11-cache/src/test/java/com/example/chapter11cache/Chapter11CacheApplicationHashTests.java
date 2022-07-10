package com.example.chapter11cache;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@Slf4j
class Chapter11CacheApplicationHashTests {
    @Resource
    private RedisTemplate redisTemplate;

    @Test
    public void hash1() {
        Map<String, Object> testmap = new HashMap<>();
        testmap.put("name", "Jayce");
        testmap.put("sex", "男");
        // 写入对象
        redisTemplate.opsForHash().putAll("hash1", testmap);
        // 读取对象
        log.info(redisTemplate.opsForHash().entries("hash1").toString()); // {sex=男, name=Jayce}
    }

    @Test
    public void hash2() {
        redisTemplate.opsForHash().put("hash2", "name", "Tony");
        redisTemplate.opsForHash().put("hash2", "sex", "男");
        log.info(redisTemplate.opsForHash().entries("hash2").toString()); // {name=Tony, sex=男}
    }

    @Test
    public void hash3() {
        log.info(redisTemplate.opsForHash().values("hash1").toString()); // [男, Jayce]
        log.info(redisTemplate.opsForHash().values("hash2").toString()); // [Tony, 男]
    }

    @Test
    public void hash4() {
        redisTemplate.opsForHash().put("hash4", "name", "Jayce");
        redisTemplate.opsForHash().put("hash4", "age", "26");
        redisTemplate.opsForHash().put("hash4", "tel", "10086");
        redisTemplate.opsForHash().put("hash4", "rm", "nothing");
        log.info(redisTemplate.opsForHash().entries("hash4").toString()); // {name=Jayce, age=26, rm=nothing, tel=10086}
//        log.info(redisTemplate.opsForHash().delete("hash4", "tel").toString()); // 1 返回删除成功属性的个数
        log.info(redisTemplate.opsForHash().delete("hash4", "tel", "rm").toString()); // 2 返回删除成功属性的个数
        log.info(redisTemplate.opsForHash().entries("hash4").toString()); // {name=Jayce, age=26}
    }

    @Test
    public void hash5() {
        redisTemplate.opsForHash().put("hash4", "name", "Jayce");
        redisTemplate.opsForHash().put("hash4", "age", "26");
        log.info(redisTemplate.opsForHash().hasKey("hash4", "age").toString()); // true
        log.info(redisTemplate.opsForHash().delete("hash4", "age").toString()); // 1
        log.info(redisTemplate.opsForHash().hasKey("hash4", "age").toString()); // false
    }

    @Test
    public void hash6() {
        log.info(redisTemplate.opsForHash().get("hash4", "name").toString()); // Jayce
    }

    @Test
    public void hash7() {
        redisTemplate.opsForHash().put("hash7", "name", "Jayce");
        redisTemplate.opsForHash().put("hash7", "age", "26");
        log.info(redisTemplate.opsForHash().keys("hash7").toString()); // [name, age]
        redisTemplate.opsForHash().put("hash7", "tel", "10086");
        redisTemplate.opsForHash().put("hash7", "rm", "nothing");
        log.info(redisTemplate.opsForHash().keys("hash7").toString()); // [name, age, tel, rm]
    }

    @Test
    public void hash8() {
        log.info(redisTemplate.opsForHash().size("hash7").toString()); // 4
        log.info(redisTemplate.opsForHash().delete("hash7", "tel", "name").toString()); // 2
        log.info(redisTemplate.opsForHash().size("hash7").toString()); // 2
    }
}
