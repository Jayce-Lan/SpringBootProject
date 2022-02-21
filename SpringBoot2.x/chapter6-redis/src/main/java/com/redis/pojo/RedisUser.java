package com.redis.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class RedisUser implements Serializable {
    private Integer id;
    private String name;
    private String pwd;
}
