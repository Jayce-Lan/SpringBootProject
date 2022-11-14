package com.example;

import com.example.entity.User;
import com.example.util.Producer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class Chapter12Rabbitmq2ApplicationTests {
	@Resource
	private Producer producer;

	@Test
	void contextLoads() {
		producer.produce();
	}

	@Test
	void testRabbitmqToObject() {
		User user = new User();
		user.setName("Jayce").setPassword("123456");
		producer.produceUser(user);
	}

}
