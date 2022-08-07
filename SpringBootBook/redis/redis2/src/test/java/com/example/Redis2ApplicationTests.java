package com.example;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
class Redis2ApplicationTests {
	@Resource
	private RedisTemplate redisTemplate;

	@Test
	void contextLoads() {
		redisTemplate.opsForValue().set("num", "666666");
		Object num = redisTemplate.opsForValue().get("num");
		log.info("num: {}", num);
	}

	@Test
	void setTest() {
		redisTemplate.opsForValue().set("name", "Tonyy");
		Object name = redisTemplate.opsForValue().get("name");
		log.info("name: {}", name);
	}

}
