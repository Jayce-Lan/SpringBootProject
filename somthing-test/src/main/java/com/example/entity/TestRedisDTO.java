package com.example.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class TestRedisDTO implements Serializable {
    private String name;
    private Integer age;
    private String job;
}
