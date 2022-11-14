package com.example.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 被生产消费的实体类，值得注意的是，一定需要序列化对线，否则会报错
 */
@Data
@Accessors(chain = true)
public class User implements Serializable {
    private String name;
    private String password;
}
