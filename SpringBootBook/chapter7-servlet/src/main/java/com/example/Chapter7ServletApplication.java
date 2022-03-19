package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class Chapter7ServletApplication {

	public static void main(String[] args) {
		SpringApplication.run(Chapter7ServletApplication.class, args);
	}

}
