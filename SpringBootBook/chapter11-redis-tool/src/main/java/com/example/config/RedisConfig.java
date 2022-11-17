package com.example.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.lang.reflect.Method;

/**
 * RedisConfig类为Redis设置了一些全局配置，
 * 比如配置主键的生产策略KeyGenerator()方法，
 * 此类继承CachingConfigurerSupport类，并重写方法keyGenerator()，
 * 如果不配置，就默认使用参数名作为主键
 */

@Configuration
@EnableCaching
@Slf4j
public class RedisConfig extends CachingConfigurerSupport {
    /**
     * 采用 RedisCacheManager 作为缓存管理器
     * 为了处理高可用 Redis，可以使用 RedisSentinelConfiguration 来支持 RedisSentinel
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheManager redisCacheManager = RedisCacheManager.builder(redisConnectionFactory).build();
        return redisCacheManager;
    }

    /**
     * 重写父类方法，自定义生成key的规则
     * @return
     */
    @Override
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                // 格式化缓存key字符串
                StringBuilder stringBuilder = new StringBuilder();
                // 追加类名
                stringBuilder.append(target.getClass().getName());
                // 追加方法名
                stringBuilder.append(method.getName());
                // 遍历参数并追加
                for (Object obj : params) {
                    stringBuilder.append(obj.toString());
                }
                log.info("调用Redis缓存key >>>>>>>>>> {}", stringBuilder.toString());
                return stringBuilder.toString();
            }
        };
    }
}
