package com.springioc.config;

import com.springioc.pojo.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 通过配置文件识别Bean，并赋值的方式装备Bean
 */
@Configuration
public class AppConfig4User {
    @Bean(name = "user")
    public User initUser() {
        User user = new User();
        user.setId("no1");
        user.setName("Tom");
        user.setNote("note1");
        return user;
    }
}
