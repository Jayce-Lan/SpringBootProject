package com.example;

import com.example.read.ReadFile;
import com.example.util.CommonDict;
import com.example.util.XmlUtils;
import com.example.xmltest.TestXMLToObjBO;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.io.IOException;

@SpringBootTest
@Slf4j
class SomthingTestApplicationTests {
	@Resource
	private StringRedisTemplate stringRedisTemplate;

	@Test
	void testReadFileReadTxtByReadAllLines() {
		ReadFile readFile = new ReadFile();
		String path = "/Users/lanjiesi/Documents/test/sspl.txt";
		try {
			readFile.readTxtByReadAllLines(path);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	@Test
	void testReadFileReadTxtByReadAllLines2() {
		ReadFile readFile = new ReadFile();
		// 生死疲劳 100
//		String path = "/Users/lanjiesi/Documents/test/sspl.txt";
		// 在细雨中呼喊 100-150
		String path = "/Users/lanjiesi/Documents/test/zxyzhhh.txt";
		try {
			readFile.readTxtByReadAllLines2(path, 100, 150);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	@Test
	void testXMLToObj() throws JsonProcessingException {
		String xml = "<HEAD><PSN_NO>123123</PSN_NO><AGE>11</AGE></HEAD>";
		TestXMLToObjBO testXMLToObjBO = XmlUtils.xmlStrToObject(xml, TestXMLToObjBO.class);
		log.info("testXMLToObjBO, {}", testXMLToObjBO);
	}

	@Test
	void setRedisValue() {
		stringRedisTemplate.opsForValue().set(CommonDict.REDIS_KEY_NUM, "50");
	}
}
