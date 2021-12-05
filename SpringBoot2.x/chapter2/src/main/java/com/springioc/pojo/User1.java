package com.springioc.pojo;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 通过扫描装配Bean
 */
@Data
// 声明需要被IOC容器扫描装配，并且给予Bean名称为user1，如果不加入，那么默认将类的首字母做小写进行装配
@Component("user1")
public class User1 {
    // 使用 @Value 注解去指定具体的值
    @Value("nou1")
    private String id;
    @Value("Jack")
    private String name;
    @Value("noteu1")
    private String note;
}
