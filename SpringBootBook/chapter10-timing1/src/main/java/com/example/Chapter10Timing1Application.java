package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Chapter10Timing1Application {

	public static void main(String[] args) {
		SpringApplication.run(Chapter10Timing1Application.class, args);
	}

}
