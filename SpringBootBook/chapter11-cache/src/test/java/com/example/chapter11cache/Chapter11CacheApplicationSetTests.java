package com.example.chapter11cache;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Slf4j
public class Chapter11CacheApplicationSetTests {
    @Resource
    RedisTemplate redisTemplate;

    @Test
    void set1() {
        String[] strings = {"str1", "str2", "str3", "str1"};
        log.info("set1 {}", redisTemplate.opsForSet().add("set1", strings)); // 3 插入集合的长度
        log.info("set1 {}", redisTemplate.opsForSet().members("set1")); // [str2, str1, str3]
    }

    @Test
    void set2() {
        String[] strings = {"1", "2", "3", "4", "5", "6", "7"};
        redisTemplate.opsForSet().add("set2", strings);
        log.info("set2 {}", redisTemplate.opsForSet().members("set2")); // [2, 4, 5, 7, 3, 6, 1]
        log.info("set2.remove {}", redisTemplate.opsForSet().remove("set2", "1")); // 1
        log.info("set2 {}", redisTemplate.opsForSet().members("set2")); // [4, 5, 7, 3, 6, 2]
        String[] removeStrs = {"2", "3", "4"};
        log.info("set2.remove {}", redisTemplate.opsForSet().remove("set2", removeStrs)); // 3
        log.info("set2 {}", redisTemplate.opsForSet().members("set2")); // [5, 7, 6]
        log.info("set2.remove {}", redisTemplate.opsForSet().remove("set2", strings)); // 3
        log.info("set2 {}", redisTemplate.opsForSet().members("set2")); // [] (并且在redis中会被销毁)
    }

    @Test
    void set3() {
        String[] strings = {"1", "2", "3", "4", "5", "6", "7"};
        redisTemplate.opsForSet().add("set2", strings);
        log.info("set2 {}", redisTemplate.opsForSet().members("set2")); // [2, 4, 5, 7, 3, 6, 1]

        log.info("set2.pop {}",redisTemplate.opsForSet().pop("set2")); // 5
        log.info("set2 {}", redisTemplate.opsForSet().members("set2")); // [4, 7, 3, 6, 1, 2]

        log.info("set2.pop {}",redisTemplate.opsForSet().pop("set2")); // 2
        log.info("set2 {}", redisTemplate.opsForSet().members("set2")); // [4, 7, 3, 6, 1]

        log.info("set2.pop {}",redisTemplate.opsForSet().pop("set2")); // 3
        log.info("set2 {}", redisTemplate.opsForSet().members("set2")); // [4, 7, 6, 1]
    }

    @Test
    void set4() {
        String[] strings = {"1", "2", "3", "4", "5", "6", "7"};
        redisTemplate.opsForSet().add("SET_4_1", strings);
        log.info("set4-1 {}", redisTemplate.opsForSet().members("SET_4_1")); // [2, 4, 5, 7, 3, 6, 1]

        // 将SET_4_1的元素移动到SET_4_2，当被移动元素 "8" 不存在时，返回 false；如果SET_4_2不存在，则不会被创建
        log.info("set4.move {}", redisTemplate.opsForSet().move("SET_4_1", "8", "SET_4_2"));
        // [4, 5, 7, 3, 6, 1, 2]
        log.info("set4-1 {}", redisTemplate.opsForSet().members("SET_4_1"));
        // []
        log.info("set4-2 {}", redisTemplate.opsForSet().members("SET_4_2"));
        // 当 "1" 存在时返回 true；如果SET_4_2不存在，则会被创建
        log.info("set4.move {}", redisTemplate.opsForSet().move("SET_4_1", "1", "SET_4_2"));
        // [4, 5, 7, 3, 6, 2]
        log.info("set4-1 {}", redisTemplate.opsForSet().members("SET_4_1"));
        // [1]
        log.info("set4-2 {}", redisTemplate.opsForSet().members("SET_4_2"));
    }

    @Test
    void set5() {
        String[] strings = {"1", "2", "3", "4", "5", "6", "7"};

        log.info("set5 {}", redisTemplate.opsForSet().add("set5", strings)); // 7
        log.info("set5.size {}", redisTemplate.opsForSet().size("set5")); // 7

        String[] addStrs = {"1", "8", "9"};

        log.info("set5 {}", redisTemplate.opsForSet().add("set5", addStrs)); // 2
        log.info("set5.size {}", redisTemplate.opsForSet().size("set5")); // 9
    }

    @Test
    void set6() {
        Cursor<String> set5 = redisTemplate.opsForSet().scan("set5", ScanOptions.NONE);
        while (set5.hasNext()) {
            log.info("set5.next {}", set5.next());
        }
    }

    @Test
    void set7() {
        String[] strings = {"1", "2", "3", "4", "5", "6", "7"};
        List<String> list = new ArrayList<>(Arrays.asList(strings));
        String[] array = list.toArray(new String[list.size()]);
        log.info("set7 {}", redisTemplate.opsForSet().add("set7", array));
        log.info("set7 {}", redisTemplate.opsForSet().members("set7"));
    }
}
