package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

/**
 * 整合dev的配置文件以供主配置类识别
 */
@Profile("dev")
@Configuration
@PropertySource(value = "classpath:testconfig/db-dev.properties", ignoreResourceNotFound = true)
@PropertySource(value = "classpath:testconfig/mq-dev.properties", ignoreResourceNotFound = true)
public class PropertyDevConfig {
}
