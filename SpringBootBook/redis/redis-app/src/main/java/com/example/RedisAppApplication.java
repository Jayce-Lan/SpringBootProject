package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy(exposeProxy = true) // 暴露代理对象
@SpringBootApplication
public class RedisAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedisAppApplication.class, args);
	}

}
