package com.example;

import com.example.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;

@SpringBootTest
@Slf4j
class Redis2ApplicationTests {
	@Resource
	private RedisTemplate<String, Object> redisTemplate;

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

	/**
	 * 使用序列化存储对象
	 * 在redis中，对象被存储成了json格式
	 */
	@Test
	void saveObject() {
		redisTemplate.opsForValue().set("user:100", new User("卢本伟", new BigDecimal(19)));
		User user1 = (User) redisTemplate.opsForValue().get("user1");
		log.info("user1: {}", user1);
	}

}
