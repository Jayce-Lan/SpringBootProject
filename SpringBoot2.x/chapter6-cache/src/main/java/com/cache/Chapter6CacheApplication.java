package com.cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
// 开启缓存
@EnableCaching
public class Chapter6CacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(Chapter6CacheApplication.class, args);
	}

}
