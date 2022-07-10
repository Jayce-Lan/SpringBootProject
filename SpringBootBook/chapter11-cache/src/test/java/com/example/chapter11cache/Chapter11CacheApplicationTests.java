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

	/**
	 * 设置偏移量
	 */
	@Test
	public void offsetTest() {
		redisTemplate.opsForValue().set("offsetKey", "Hello World", 6);
		System.err.println(redisTemplate.opsForValue().get("offsetKey"));
	}

	/**
	 * 使用 getAndSet 在获取旧的值时重新获取新的值
	 */
	@Test
	public void testGetAndSet() {
		redisTemplate.opsForValue().set("getAndSetTest", "Test");
		// 在此处向外输出旧的值，并同时获取到新的值
		log.info((redisTemplate.opsForValue().getAndSet("getAndSetTest", "Test2")).toString()); // Test
		log.info(redisTemplate.opsForValue().get("getAndSetTest").toString()); // Test2
	}

	/**
	 * 使用 append 向值后面追加值
	 */
	@Test
	public void appendTest() throws InterruptedException {
		redisTemplate.opsForValue().set("appendTest", "Test");
		log.info(redisTemplate.opsForValue().get("appendTest").toString()); // Test
		redisTemplate.opsForValue().append("appendTest", "appendTest");
		log.info(redisTemplate.opsForValue().get("appendTest").toString()); // TestappendTest
	}

	/**
	 * 使用 size 返回 key 所对应的 value 的长度
	 * 返回的是二进制的大小（可到Redis管理工具查看）
	 */
	@Test
	public void sizeTest() {
		redisTemplate.opsForValue().set("sizeTest", "Test");
		log.info(redisTemplate.opsForValue().size("sizeTest").toString()); // 11
	}
}
