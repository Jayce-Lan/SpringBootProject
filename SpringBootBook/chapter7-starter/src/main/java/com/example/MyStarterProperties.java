package com.example;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.mystarter")
@Data
public class MyStarterProperties {
    private String parameter;
}
