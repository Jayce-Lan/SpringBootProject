package com.example;

import com.example.entity.User;
import com.example.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
public class RedisConfigTests {
    @Resource
    private UserService userService;

    /**
     * 二次查询时由于首次写入了缓存，因此不回再查询数据库
     */
    @Test
    void testCache() {
        User userById = userService.getUserById("6");
        log.info("首次查询 >>>>>>>>>> {}", userById);
        User userById2 = userService.getUserById("6");
        log.info("二次查询 >>>>>>>>>> {}", userById2);
    }
}
