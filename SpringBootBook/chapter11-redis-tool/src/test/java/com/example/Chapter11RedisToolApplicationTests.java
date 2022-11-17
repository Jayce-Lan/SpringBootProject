package com.example;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
class Chapter11RedisToolApplicationTests {
	@Resource
	private StringRedisTemplate stringRedisTemplate;

	@Test
	void contextLoads() {
		stringRedisTemplate.opsForValue().set("hello", "测试Redis");
		String hello = stringRedisTemplate.opsForValue().get("hello");
		log.info("执行插入测试 >>>>>>>>>> {}", hello);
	}

}
