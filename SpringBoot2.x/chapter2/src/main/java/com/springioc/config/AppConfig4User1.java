package com.springioc.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Properties;

@Slf4j
@Configuration
// 声明配置文件可以检索的包，该包内的所有文件都可以被IOC容器扫描，excludeFilters声明@Service将不被扫描
@ComponentScan(basePackages = "com.springioc.*", excludeFilters = {@ComponentScan.Filter(classes = {Service.class})})
public class AppConfig4User1 {
    @Bean("dataSource")
    public DataSource getDataSource() {
        Properties properties = new Properties();
        properties.getProperty("driver", "com.mysql.jdbc.Driver");
        properties.getProperty("url", "127.0.0.1");
        properties.getProperty("username", "root");
        properties.getProperty("password", "root");
        DataSource dataSource = null;
        try {
            dataSource = BasicDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("这行语句被执行了");
        return dataSource;
    }
}
