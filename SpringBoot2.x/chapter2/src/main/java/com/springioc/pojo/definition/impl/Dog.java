package com.springioc.pojo.definition.impl;

import com.springioc.pojo.definition.Animal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Dog implements Animal {
    @Override
    public void use() {
        log.info("狗【" + Dog.class.getSimpleName() + "】是看门用的。");
    }
}
