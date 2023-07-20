package com.example.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 测试缓存的实体类
 */
@Data
    @Accessors(chain = true)
public class UserToCacheDTO implements Serializable {
    private String id;
    private String name;
    private Integer age;
}
