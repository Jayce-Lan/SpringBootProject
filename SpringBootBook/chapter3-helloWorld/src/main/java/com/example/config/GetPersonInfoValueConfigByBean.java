package com.example.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@Data
public class GetPersonInfoValueConfigByBean {
    private String name;
    private int age;

    @Bean
    @ConfigurationProperties(prefix = "personinfo2")
    public GetPersonInfoValueConfigByBean getPersonInfoValueConfigByBean() {
        return new GetPersonInfoValueConfigByBean();
    }
}
