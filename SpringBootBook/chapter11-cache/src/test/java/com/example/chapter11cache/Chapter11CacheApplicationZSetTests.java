package com.example.chapter11cache;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

@SpringBootTest
@Slf4j
public class Chapter11CacheApplicationZSetTests {
    @Resource
    RedisTemplate redisTemplate;

    @Test
    void zset1() {
        // 后面的 9.6 为被关联的double类型分数，进入zset后按照这个分数从小到大进行排序
        ZSetOperations.TypedTuple<String> tuple1 = new DefaultTypedTuple<>("zset-1", 9.6);
        ZSetOperations.TypedTuple<String> tuple2 = new DefaultTypedTuple<>("zset-2", 9.9);
        ZSetOperations.TypedTuple<String> tuple3 = new DefaultTypedTuple<>("zset-3", 9.8);
        Set<ZSetOperations.TypedTuple<String>> tuples = new HashSet<>();
        tuples.add(tuple1);
        tuples.add(tuple2);
        tuples.add(tuple3);
        log.info("zset1 {}", redisTemplate.opsForZSet().add("zset1", tuples)); // 3
        /**
         * [zset-1, zset-3, zset-2]
         * 由于 zset-2 的 score 为9.9，zset-3 的 score 为 9.8
         * 因此 按照score排序，zset-2 排在 zset-3 后面
         */
        log.info("zset1 {}", redisTemplate.opsForZSet().range("zset1", 0, -1));
    }

    @Test
    void zset2() {
        log.info("zset2 {}", redisTemplate.opsForZSet().add("zset2", "zset-1", 1.0)); // true
        log.info("zset2 {}", redisTemplate.opsForZSet().add("zset2", "zset-1", 1.0)); // false
        log.info("zset2 {}", redisTemplate.opsForZSet().add("zset2", "zset-2", 1.0)); // true
        log.info("zset2 {}", redisTemplate.opsForZSet().add("zset2", "zset-3", 1.0)); // true
        log.info("zset2 {}", redisTemplate.opsForZSet().add("zset2", "zset-4", .5)); // true
        log.info("zset2 {}", redisTemplate.opsForZSet().add("zset2", "zset-5", .5)); // true
        log.info("zset2 {}", redisTemplate.opsForZSet().add("zset2", "zset-6", .8)); // true
        // [zset-4, zset-5, zset-6, zset-1, zset-2, zset-3]
        log.info("zset2 {}", redisTemplate.opsForZSet().range("zset2", 0, -1));
    }

    @Test
    void zset3() {
        // [zset-4, zset-5, zset-6, zset-1, zset-2, zset-3]
        log.info("zset2 {}", redisTemplate.opsForZSet().range("zset2", 0, -1));
        // 1
        log.info("zset2 {}", redisTemplate.opsForZSet().remove("zset2", "zset-1"));
        // 1
        log.info("zset2 {}", redisTemplate.opsForZSet().remove("zset2", "zset-6", "zset-7"));
        // [zset-4, zset-5, zset-2, zset-3]
        log.info("zset2 {}", redisTemplate.opsForZSet().range("zset2", 0, -1));
        // 2
        log.info("zset2 {}", redisTemplate.opsForZSet().remove("zset2", "zset-3", "zset-4"));
        // [zset-5, zset-2]
        log.info("zset2 {}", redisTemplate.opsForZSet().range("zset2", 0, -1));
    }

    @Test
    void zset4() {
        log.info("zset4 {}", redisTemplate.opsForZSet().add("zset4", "1", 1.0));
        log.info("zset4 {}", redisTemplate.opsForZSet().add("zset4", "2", 1.0));
        log.info("zset4 {}", redisTemplate.opsForZSet().add("zset4", "3", 1.0));
        log.info("zset4 {}", redisTemplate.opsForZSet().add("zset4", "4", 1.0));
        log.info("zset4 {}", redisTemplate.opsForZSet().add("zset4", "5", 1.0));
        log.info("zset4 {}", redisTemplate.opsForZSet().add("zset4", "6", 1.0));
        // [1, 2, 3, 4, 5, 6]
        log.info("zset4 {}", redisTemplate.opsForZSet().range("zset4", 0, -1));
        log.info("zset4.rank {}", redisTemplate.opsForZSet().rank("zset4", "1")); // 0
        log.info("zset4.rank {}", redisTemplate.opsForZSet().rank("zset4", "7")); // null
        log.info("zset4.rank {}", redisTemplate.opsForZSet().rank("zset4", "2")); // 1
    }

    @Test
    void zset5() {
        ZSetOperations.TypedTuple<String> tuple1 = new DefaultTypedTuple<>("1", 1.0);
        ZSetOperations.TypedTuple<String> tuple2 = new DefaultTypedTuple<>("2", 2.0);
        ZSetOperations.TypedTuple<String> tuple3 = new DefaultTypedTuple<>("3", 2.2);
        ZSetOperations.TypedTuple<String> tuple4 = new DefaultTypedTuple<>("4", 3.0);
        ZSetOperations.TypedTuple<String> tuple5 = new DefaultTypedTuple<>("5", 3.2);
        Set<ZSetOperations.TypedTuple<String>> tuples = new HashSet<>();
        tuples.add(tuple1);
        tuples.add(tuple2);
        tuples.add(tuple3);
        tuples.add(tuple4);
        tuples.add(tuple5);
//        log.info("zset5 {}", redisTemplate.opsForZSet().add("zset5", tuples)); // 5
        log.info("zset5.count {}", redisTemplate.opsForZSet().count("zset5", 2, 3)); // 3
        log.info("zset5.count {}", redisTemplate.opsForZSet().rangeByScore("zset5", 2, 3)); // [2, 3, 4]

        log.info("zset5.count {}", redisTemplate.opsForZSet().count("zset5", 1, 2.2)); // 3
        log.info("zset5.count {}", redisTemplate.opsForZSet().rangeByScore("zset5", 1, 2.2)); // [1, 2, 3]

        log.info("zset5.size {}", redisTemplate.opsForZSet().size("zset5")); // 5

        log.info("zset5.score {}", redisTemplate.opsForZSet().score("zset5", "1")); // 1.0
        log.info("zset5.score {}", redisTemplate.opsForZSet().score("zset5", "3")); // 2.2
        log.info("zset5.score {}", redisTemplate.opsForZSet().score("zset5", "7")); // null
    }

    @Test
    void set6() {
        redisTemplate.opsForZSet().add("zset6", "1", 1.0);
        redisTemplate.opsForZSet().add("zset6", "2", .8);
        redisTemplate.opsForZSet().add("zset6", "3", 1.0);
        redisTemplate.opsForZSet().add("zset6", "4", .9);
        redisTemplate.opsForZSet().add("zset6", "5", 5.0);
        redisTemplate.opsForZSet().add("zset6", "6", 5.0);
        redisTemplate.opsForZSet().add("zset6", "7", 5.0);
        log.info("zset6 {}", redisTemplate.opsForZSet().range("zset6", 0, -1)); // [2, 4, 1, 3, 5, 6, 7]
        // 删除第0位开始，第2位结束的元素，返回删除元素个数
        log.info("zset6.removeRange {}", redisTemplate.opsForZSet().removeRange("zset6", 0, 2)); // 3
        log.info("zset6 {}", redisTemplate.opsForZSet().range("zset6", 0, -1)); // [2, 4, 1, 3, 5, 6, 7]
        log.info("zset6.removeRange {}", redisTemplate.opsForZSet().removeRange("zset6", 0, 6)); // 4
        log.info("zset6 {}", redisTemplate.opsForZSet().range("zset6", 0, -1)); // []
    }

    @Test
    void set7() {
        redisTemplate.opsForZSet().add("zset6", "1", 1.0);
        redisTemplate.opsForZSet().add("zset6", "2", .8);
        redisTemplate.opsForZSet().add("zset6", "3", 1.0);
        redisTemplate.opsForZSet().add("zset6", "4", .9);
        redisTemplate.opsForZSet().add("zset6", "5", 5.0);
        redisTemplate.opsForZSet().add("zset6", "6", 5.0);
        redisTemplate.opsForZSet().add("zset6", "7", 5.0);
        // 此处泛型亦可使用 String，因为这是一个纯字符串集合
        Cursor<ZSetOperations.TypedTuple<Object>> zset6 = redisTemplate.opsForZSet().scan("zset6", ScanOptions.NONE);
        while (zset6.hasNext()) {
            log.info("zset6  {}", zset6.next());
        }
        /**
         * 执行结果
         * [score=0.8, value=2]
         * [score=0.9, value=4]
         * [score=1.0, value=1]
         * [score=1.0, value=3]
         * [score=5.0, value=5]
         * [score=5.0, value=6]
         * [score=5.0, value=7]
         */
    }
}
