package com.example.config;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RedissonConfig {
    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private String redisPort;
    @Value("${spring.redis.database}")
    private Integer redisDatabase;

    /**
     * 配置redisson
     * @return 返回redisson，并注入至环境中
     */
    @Bean
    public Redisson redisson() {
        Config config = new Config();
        config.useSingleServer().setAddress(this.getRedisHostAndPortToSetAddress())
                .setDatabase(this.redisDatabase);
        log.info("redisson依赖注入成功！地址为 >>>>> {}", config.useSingleServer().getAddress());
        log.info("redisson依赖注入成功！数据库为 >>>>> {}", config.useSingleServer().getDatabase());
        return (Redisson) Redisson.create(config);
    }

    /**
     * 获取一个可以给redisson进行写入的地址
     * 地址格式：redis://host:port
     * @return 返回可以供redisson写入的地址
     */
    private String getRedisHostAndPortToSetAddress() {
        return "redis://" + this.redisHost + ":" + this.redisPort;
    }
}
