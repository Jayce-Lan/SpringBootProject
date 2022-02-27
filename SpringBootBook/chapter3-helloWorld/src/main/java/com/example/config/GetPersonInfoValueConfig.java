package com.example.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 使用 @ConfigurationProperties 注解获取配置文件中的值
 */

@Data
@Component
@ConfigurationProperties(prefix = "personinfo")
public class GetPersonInfoValueConfig {
    private String name;
    private int age;
}
