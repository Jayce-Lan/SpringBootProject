package com.example.util;

import com.example.entity.TestRedisDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
public class TestRedisCacheUtil {
    @Resource
    private RedisCacheUtil redisCacheUtil;

    @Test
    void testSetCacheObjcet() {
        TestRedisDTO testRedisDTO = new TestRedisDTO();
        testRedisDTO.setAge(18);
        testRedisDTO.setName("Jack");
        testRedisDTO.setJob("coder");
        testRedisDTO.setJob("code")
                .setName("Tom")
                .setAge(20);
        redisCacheUtil.setCacheObjectToString("jack", testRedisDTO);
    }
}
