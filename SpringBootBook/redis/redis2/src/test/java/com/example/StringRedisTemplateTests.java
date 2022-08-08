package com.example;

import com.example.pojo.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;

@SpringBootTest
@Slf4j
public class StringRedisTemplateTests {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    // Spring的序列化工具，用于对象与json的互转
    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    void setTest() {
        stringRedisTemplate.opsForValue().set("name", "JONNY");
        String name = stringRedisTemplate.opsForValue().get("name");
        log.info("name: {}",name);
    }

    /**
     * 使用字符串序列化写入对象
     */
    @Test
    void setObjectTest() throws JsonProcessingException {
        User user = new User("张伟", new BigDecimal(30));
        // 序列化对象
        String userString = mapper.writeValueAsString(user);
        stringRedisTemplate.opsForValue().set("user:200", userString);
        String userJson = stringRedisTemplate.opsForValue().get("user:200");
        // 取出之后需要反序列化
        User user1 = mapper.readValue(userJson, User.class);
        log.info("user:200 - {}", user1);
    }

    @Test
    void saveHash1Test() {
        stringRedisTemplate.opsForHash().put("user:400", "name", "蔡徐坤");
        stringRedisTemplate.opsForHash().put("user:400", "age", "30");

        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries("user:400");
        log.info("user:400 - {}", entries);
    }
}
