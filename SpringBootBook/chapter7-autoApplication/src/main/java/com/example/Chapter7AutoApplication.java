package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;

// 将下面的注解拆分
//@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Chapter7AutoApplication {

	public static void main(String[] args) {
//		SpringApplication.run(Chapter7AutoApplication.class, args);
		getAutoApplicationAndRun(args);
	}

	/**
	 * 改造 run 方法完成自动装配
	 * @param args
	 */
	public static void getAutoApplicationAndRun(String[] args) {
		SpringApplication springApplication = new SpringApplication(Chapter7AutoApplication.class);
		springApplication.run(args);
	}

}
