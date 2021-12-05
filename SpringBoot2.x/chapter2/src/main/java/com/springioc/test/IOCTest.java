package com.springioc.test;

import com.springioc.config.AppConfig4User;
import com.springioc.config.AppConfig4User1;
import com.springioc.pojo.User;
import com.springioc.pojo.User1;
import com.springioc.service.User1Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.sql.DataSource;

@Slf4j
public class IOCTest {
    public static void main(String[] args) {
        logAppConfig4User();
        logAppConfig4User1();
    }

    /**
     * 执行AppConfig的配置文件
     */
    public static void logAppConfig4User() {
        // 读取 AppConfig
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig4User.class);
        // 获取文件中的 Bean
        User user = ctx.getBean(User.class);
        log.info(String.valueOf(user));
    }

    public static void logAppConfig4User1() {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig4User1.class);
        User1 user1 = context.getBean(User1.class);
        log.info(user1.toString());

//        User1Service user1Service = context.getBean(User1Service.class);
        // 由于不装备@Service的Bean，因此会报错
        // No qualifying bean of type 'com.springioc.service.User1Service' available
//        user1Service.printUser1(user1);
        DataSource dataSource = context.getBean(DataSource.class);
        log.info(String.valueOf(dataSource));
    }
}
