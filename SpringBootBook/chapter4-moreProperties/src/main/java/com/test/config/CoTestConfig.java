package com.test.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "personinfo")
public class CoTestConfig {
    private String name;
    private int age;
    private List<String> address;
}