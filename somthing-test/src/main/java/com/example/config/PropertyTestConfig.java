package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

/**
 * 整合test的配置文件以供主配置类识别
 */
@Profile("test")
@Configuration
@PropertySource(value = "classpath:testconfig/db-test.properties", ignoreResourceNotFound = true)
@PropertySource(value = "classpath:testconfig/mq-test.properties", ignoreResourceNotFound = true)
public class PropertyTestConfig {
}
