package com.example;

import com.example.service.WeatherDataService;
import com.example.vo.WeatherResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
class MicroWeatherBasicApplicationTests {
	@Resource
	private WeatherDataService weatherDataService;

	@Test
	void contextLoads() {
		WeatherResponse response = weatherDataService.getDataByCityName("南宁市");
		log.info("执行结果 >>>>>> {}", response);
	}

}
