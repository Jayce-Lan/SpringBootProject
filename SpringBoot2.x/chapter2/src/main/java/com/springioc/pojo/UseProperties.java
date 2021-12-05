package com.springioc.pojo;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 使用属性文件注入依赖
 */
@Slf4j
@Getter
@Configuration
//@ConfigurationProperties("database")
public class UseProperties {
    @Value("${database.driverName}")
    private String driverName;

    @Value("${database.url}")
    private String url;

    private String username;
    private String password;

    public void setDriverName(String driverName) {
        log.info(driverName);
        this.driverName = driverName;
    }

    public void setUrl(String url) {
        log.info(url);
        this.url = url;
    }

    @Value("${database.username}")
    public void setUsername(String username) {
        log.info(username);
        this.username = username;
    }

    @Value("${database.password}")
    public void setPassword(String password) {
        log.info(password);
        this.password = password;
    }
}
