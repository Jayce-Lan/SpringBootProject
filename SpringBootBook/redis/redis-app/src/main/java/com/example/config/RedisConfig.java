package com.example.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 用于配置Redisson客户端的配置类
 */

@Configuration
public class RedisConfig {
    @Bean
    public RedissonClient redissonClient() {
        // 配置类
        Config config = new Config();
        // 添加redis地址，也可以使用 config.useClusterServers() 添加集群地址
        config.useSingleServer().setAddress("redis://127.0.0.1:6379").setDatabase(1);
        // 创建客户端
        return Redisson.create(config);
    }
}
