package com.example;

import com.example.config.GetPersonInfoValueConfig;
import com.example.config.GetPersonInfoValueConfigByBean;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class Chapter3HelloWorldApplicationTests {
    @Value("${age}")
    private int age;

    @Resource
    private GetPersonInfoValueConfig getPersonInfoValueConfig;

    @Resource
    private GetPersonInfoValueConfigByBean getPersonInfoValueConfigByBean;

    @Test
    void contextLoads() {
    }

    @Test
    void testYML() {
        System.out.println(this.age);
    }

    @Test
    void testGetPersonInfoValueConfig() {
        System.out.println(this.getPersonInfoValueConfig.getAge() + getPersonInfoValueConfig.getName());
    }

    @Test
    void testGetPersonInfoValueConfigByBean() {
        System.out.println(getPersonInfoValueConfigByBean.getName() + getPersonInfoValueConfigByBean.getAge());
    }
}
