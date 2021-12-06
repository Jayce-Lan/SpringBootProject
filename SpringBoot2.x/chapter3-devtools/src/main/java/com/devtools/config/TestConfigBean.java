package com.devtools.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 直接将配置文件中的参数注入到Bean中
 */
@Data
@Component
@ConfigurationProperties(prefix = "test")
public class TestConfigBean {
    private String name;
    private int intValue;
    private String uuid;
    private int randomNumber;
}
