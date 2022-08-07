package com.example.redis1;

import com.example.redis1.util.JedisConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;

@SpringBootTest
@Slf4j
class Redis1ApplicationTests {
	private Jedis jedis;

	@BeforeEach
	void setUp() {
		// 设置连接
//		jedis = new Jedis("127.0.0.1", 6379);
		// 使用连接池获取
		jedis = JedisConnectionFactory.getJedis();
		// 设置密码
		jedis.auth("123456");
		// 使用数据库
		jedis.select(0);
	}

	@Test
	void testSet() {
		String num = jedis.set("num", "123456");
		String num1 = jedis.get("num");
		log.info("num: {}", num);
		log.info("num1: {}", num1);
	}

	/**
	 * 使用结束后关闭连接
	 */
	@AfterEach
	void tearDown() {
		if (jedis != null) {
			jedis.close();
		}
	}

	@Test
	void contextLoads() {
	}

}
