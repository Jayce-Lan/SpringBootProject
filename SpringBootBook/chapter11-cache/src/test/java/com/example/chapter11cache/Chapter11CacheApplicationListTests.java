package com.example.chapter11cache;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
public class Chapter11CacheApplicationListTests {
    @Resource
    RedisTemplate redisTemplate;

    @Test
    void list1() {
        String[] strings = new String[]{"1", "2", "3"};
        redisTemplate.opsForList().leftPushAll("list1", strings); // 每次执行都将会再次插入数组
        log.info(redisTemplate.opsForList().range("list1", 0, -1).toString()); // [3, 2, 1]
    }

    @Test
    void list2() {
        log.info(redisTemplate.opsForList().size("list1").toString()); // 6(因为刚刚执行了两次list1插入操作，因此为6)
        log.info(redisTemplate.opsForList().size("list2").toString()); // 0
        /**
         * 下列执行报错：
         * org.springframework.data.redis.RedisSystemException: Error in execution; nested exception is io.lettuce.core.
         * RedisCommandExecutionException: WRONGTYPE Operation against a key holding the wrong kind of value
         */
        log.info(redisTemplate.opsForList().size("hash1").toString());
    }

    @Test
    void list3() {
        redisTemplate.opsForList().leftPush("list3", "1");
        log.info("list3[{}]", redisTemplate.opsForList().range("list3", 0, -1)); // [1]
        log.info("list3.size==={}", redisTemplate.opsForList().size("list3")); // 1
        redisTemplate.opsForList().leftPush("list3", "2");
        log.info("list3[{}]", redisTemplate.opsForList().range("list3", 0, -1)); // [2, 1]
        log.info("list3.size==={}", redisTemplate.opsForList().size("list3")); // 2
        redisTemplate.opsForList().leftPush("list3", "3");
        log.info("list3[{}]", redisTemplate.opsForList().range("list3", 0, -1)); // [3, 2, 1]
        log.info("list3.size==={}", redisTemplate.opsForList().size("list3")); // 3
    }

    @Test
    void list4() {
        redisTemplate.opsForList().rightPush("list4", "1");
        log.info("list4: {}", redisTemplate.opsForList().range("list4", 0 ,-1)); // [1]
        log.info("list4.size: {}", redisTemplate.opsForList().size("list4")); // 1
        redisTemplate.opsForList().rightPush("list4", "2");
        log.info("list4: {}", redisTemplate.opsForList().range("list4", 0 ,-1)); // [1, 2]
        log.info("list4.size: {}", redisTemplate.opsForList().size("list4")); // 2
        redisTemplate.opsForList().rightPush("list4", "3");
        log.info("list4: {}", redisTemplate.opsForList().range("list4", 0 ,-1)); // [1, 2, 3]
        log.info("list4.size: {}", redisTemplate.opsForList().size("list4")); // 3
    }

    @Test
    void list5() {
        String[] strings = {"1", "2", "3"};
        redisTemplate.opsForList().rightPushAll("list5", strings);
        log.info("list5: {}", redisTemplate.opsForList().range("list5", 0, -1)); // [1, 2, 3]
    }

    @Test
    void list6() {
        log.info("list5: {}", redisTemplate.opsForList().range("list5", 0, -1)); // [1, 2, 3]
        // key: 键, index: 列表的下标, value: 替换的值
        redisTemplate.opsForList().set("list5", 1, "test1");
        redisTemplate.opsForList().set("list5", 2, "test2");
        log.info("list5: {}", redisTemplate.opsForList().range("list5", 0, -1)); // [1, test1, test2]
        // 将会数组下标越界：ERR index out of range
//        redisTemplate.opsForList().set("list5", 4, "test");
    }

    @Test
    void list7() {
        String[] strings = {"0", "1", "0", "2", "0", "3", "0"};
        redisTemplate.opsForList().rightPushAll("list7", strings);
        log.info("list7: {}", redisTemplate.opsForList().range("list7", 0, -1));
        log.info("remove: {}", redisTemplate.opsForList().remove("list7", -2, "0"));
        log.info("list7: {}", redisTemplate.opsForList().range("list7", 0, -1));
    }

    @Test
    void list8() {
        String[] strings = {"1", "2", "3", "4", "5", "6"};
        log.info("rightPushAll: {}", redisTemplate.opsForList().rightPushAll("list8", strings)); // 6
        log.info("list8: {}", redisTemplate.opsForList().range("list8", 0, -1)); // [1, 2, 3, 4, 5, 6]
        log.info("list8: {}", redisTemplate.opsForList().index("list8", 3)); // 4
    }

    @Test
    void list9() {
        log.info("list8: {}", redisTemplate.opsForList().range("list8", 0, -1)); // [1, 2, 3, 4, 5, 6]
        log.info("leftPop: {}", redisTemplate.opsForList().leftPop("list8")); // 1
        log.info("list8: {}", redisTemplate.opsForList().range("list8", 0, -1)); // [2, 3, 4, 5, 6]
        log.info("rightPop: {}", redisTemplate.opsForList().rightPop("list8")); // 6
        log.info("list8: {}", redisTemplate.opsForList().range("list8", 0, -1)); // [2, 3, 4, 5]
    }

}
