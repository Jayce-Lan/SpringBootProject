package com.example;

import com.example.tut1.Tut1Sender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class Chapter12Rabbitmq3ApplicationTests {
	@Autowired
	private Tut1Sender sender;

	@Test
	void contextLoads() {
		sender.send();
	}

}
