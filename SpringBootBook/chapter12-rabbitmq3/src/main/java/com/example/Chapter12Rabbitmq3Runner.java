package com.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
public class Chapter12Rabbitmq3Runner implements CommandLineRunner {
    @Value("${tutorial.client.duration}")
    private int duration;

    @Autowired
    private ConfigurableApplicationContext ctx;

    @Override
    public void run(String... args) throws Exception {
        log.info("Ready ... running for {} ms", duration);
        Thread.sleep(duration);
        ctx.close();
    }
}
