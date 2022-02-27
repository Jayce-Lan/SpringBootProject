package com.test;

import com.test.config.CoTestConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
class Chapter4MorePropertiesApplicationTests {
	@Resource
	private CoTestConfig coTestConfig;

	@Value("${myenvironment.name}")
	private String springAction;

	@Test
	void contextLoads() {
	}

	@Test
	void testCoTestConfig() {
		log.info(this.springAction);
		log.info(coTestConfig.getName() + coTestConfig.getAge());
		for (String address : coTestConfig.getAddress()) {
			log.info(address);
		}
	}

}
