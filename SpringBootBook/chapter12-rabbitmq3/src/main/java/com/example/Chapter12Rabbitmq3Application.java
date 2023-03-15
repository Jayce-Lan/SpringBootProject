package com.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@Slf4j
public class Chapter12Rabbitmq3Application {

	@Profile("usage_message")
	@Bean
	public CommandLineRunner usage() {
		return args -> {
			log.info("This app uses Spring Profiles to control its behavior.\n");
			log.info("Options are: ");

			// 简单模式
			// 简单模式下的消费者启动方式（接收方）-tut1
//			log.info("java -jar rabbit-tutorials.jar --spring.profiles.active=hello-world,receiver");
			// 简单模式下的生产者启动方式（发送方）
//			log.info("java -jar rabbit-tutorials.jar --spring.profiles.active=hello-world,sender");

			// 工作队列（Work Queue/Task Queue 任务队列）-tut2
			// 工作队列模式下的消费者启动方式
//			log.info("java -jar rabbit-tutorials.jar --spring.profiles.active=work-queues,receiver");
			// 工作队列模式下的生产者启动方式
//			log.info("java -jar rabbit-tutorials.jar --spring.profiles.active=work-queues,sender");

			// 发布/订阅模式（Publish/Subscribe）-tut3
//			log.info("java -jar rabbit-tutorials.jar --spring.profiles.active=pub-sub,receiver");
//			log.info("java -jar rabbit-tutorials.jar --spring.profiles.active=pub-sub,sender");

			// 路由模式（Routing）-tut4
			log.info("java -jar rabbit-tutorials.jar --spring.profiles.active=routing,receiver");
			log.info("java -jar rabbit-tutorials.jar --spring.profiles.active=routing,sender");

//			log.info("java -jar rabbit-tutorials.jar --spring.profiles.active=topics,receiver");
//			log.info("java -jar rabbit-tutorials.jar --spring.profiles.active=topics,sender");
//			log.info("java -jar rabbit-tutorials.jar --spring.profiles.active=rpc,client");
//			log.info("java -jar rabbit-tutorials.jar --spring.profiles.active=rpc,server");
		};
	}

	@Profile("!usage_message")
	@Bean
	public CommandLineRunner tutorial() {
		return new Chapter12Rabbitmq3Runner();
	}

	public static void main(String[] args) {
		SpringApplication.run(Chapter12Rabbitmq3Application.class, args);
	}

}
