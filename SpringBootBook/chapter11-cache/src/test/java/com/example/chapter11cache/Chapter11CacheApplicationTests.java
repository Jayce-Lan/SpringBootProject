package com.example.chapter11cache;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@Slf4j
class Chapter11CacheApplicationTests {
	@Resource
	private RedisTemplate redisTemplate;

	@Test
	void contextLoads() {
	}

	/**
	 * Redis写入数据
	 */
	@Test
	public void redisString() {
		redisTemplate.opsForValue().set("num", 123);
		redisTemplate.opsForValue().set("string", "some string");
		Object num = redisTemplate.opsForValue().get("num");
		Object string = redisTemplate.opsForValue().get("string");
		log.info(num.toString());
		log.info(string.toString());
	}

	/**
	 * 设置三秒后失效
	 */
	@Test
	public void invaliIn3s() {
		redisTemplate.opsForValue().set("num", "123XYZ", 3, TimeUnit.SECONDS);
		try {
			Object num1 = redisTemplate.opsForValue().get("num");
			log.info(num1.toString()); // 123XYZ
			Thread.sleep(2000);
			Object num2 = redisTemplate.opsForValue().get("num");
			log.info(num2.toString()); // 123XYZ
			Thread.sleep(2000);
			Object num3 = redisTemplate.opsForValue().get("num");
			log.info(num3 == null ? "null" : num3.toString()); // null

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
