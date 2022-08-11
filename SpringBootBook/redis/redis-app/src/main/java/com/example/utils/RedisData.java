package com.example.utils;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 设置逻辑过期的对象
 * 用于防止缓存击穿
 */

@Data
public class RedisData {
    /**
     * 过期时间
     */
    private LocalDateTime expireTime;
    /**
     * 过期对象
     */
    private Object data;
}
