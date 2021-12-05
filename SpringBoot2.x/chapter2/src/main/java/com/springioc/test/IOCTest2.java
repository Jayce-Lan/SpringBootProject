package com.springioc.test;

import com.springioc.config.AppConfig4Person;
import com.springioc.config.AppConfig4UserProperties;
import com.springioc.pojo.UseProperties;
import com.springioc.pojo.definition.Person;
import com.springioc.pojo.definition.impl.BussinessPerson;
import com.springioc.pojo.definition.impl.BussinessPerson2;
import com.springioc.service.DataSource4UserProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 依赖注入
 */
@Slf4j
public class IOCTest2 {
    public static void main(String[] args) {
        testUserProperties();
    }

    public static void testAnimal() {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig4Person.class);
        Person person = context.getBean(BussinessPerson.class);
        person.service();
    }

    public static void testPerson() {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig4Person.class);
        Person person = context.getBean(BussinessPerson2.class);
        person.service();
    }

    public static void testUserProperties() {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig4UserProperties.class);
        DataSource4UserProperties useProperties = context.getBean(DataSource4UserProperties.class);
        useProperties.logDatabase();
    }
}
