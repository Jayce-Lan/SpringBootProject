package com.springioc.pojo.definition.impl;

import com.springioc.pojo.definition.Animal;
import com.springioc.pojo.definition.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Person 的实现类
 */
@Component
public class BussinessPerson implements Person {
    // 当接口与实现类为一对一时，可以使用接口的名称
//    @Autowired
//    private Animal animal;

    // 当多个实现类对应一个接口时，必须使用实现类的命名去规范Bean
//    @Autowired
//    private Animal cat;

    // 也可以使用注解声明这个类调用的是哪个Bean
//    @Autowired
//    @Qualifier("dog")
//    private Animal animal;

    private Animal animal;
    // 在构造方法中直接使用注解
    public BussinessPerson(@Autowired @Qualifier("cat")Animal animal) {
        this.animal = animal;
    };

    @Override
    public void service() {
        this.animal.use();
//        this.cat.use();
    }

    @Override
    public void setAnimal(Animal animal) {
        this.animal = animal;
//        this.cat = animal;
    }
}
