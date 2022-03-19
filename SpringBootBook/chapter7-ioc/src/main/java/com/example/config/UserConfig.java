package com.example.config;

import com.example.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {
    /**
     * 将返回值生成一个bean
     * @return
     */
    @Bean("user1")
    public User user1() {
        User user = new User();
        user.setId(1);
        user.setName("Jayce");
        return user;
    }
}
