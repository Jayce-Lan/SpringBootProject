package com.springioc.pojo.definition.impl;

import com.springioc.pojo.definition.Animal;
import com.springioc.pojo.definition.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * 生命周期
 */
@Slf4j
@Component
public class BussinessPerson2 implements Person {
    private Animal animal;

    @Override
    public void service() {
        this.animal.use();
    }

    @Override
    @Autowired
    @Qualifier("dog")
    public void setAnimal(Animal animal) {
        log.info("延迟依赖注入");
        this.animal = animal;
    }
}
