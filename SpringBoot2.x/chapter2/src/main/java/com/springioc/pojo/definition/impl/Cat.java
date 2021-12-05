package com.springioc.pojo.definition.impl;

import com.springioc.pojo.definition.Animal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Cat implements Animal {
    @Override
    public void use() {
        log.info("猫【" + Cat.class.getSimpleName() + "】是抓老鼠用的。");
    }
}
