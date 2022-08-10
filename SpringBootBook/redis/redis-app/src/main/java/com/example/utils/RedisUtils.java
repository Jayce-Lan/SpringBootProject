package com.example.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * 用于生成Id的工具类
 */
@Component
public class RedisUtils {
    @Resource
    StringRedisTemplate stringRedisTemplate;

    /**
     * 根据序列化生成id
     * @param idName
     * @return
     */
    public String getIdUtil(String idName) {
        String id = stringRedisTemplate.opsForValue().get(idName);
        // 如果不存在，则生成新的id
        if (!StringUtils.hasText(id)) {
            Integer idToInteger = 0;
            stringRedisTemplate.opsForValue().set(idName, String.valueOf(idToInteger));
            return String.valueOf(idToInteger);
        }
        // 如果存在，则取id
        Integer idToInteger = new Integer(id);
        idToInteger++;
        stringRedisTemplate.opsForValue().set(idName, String.valueOf(idToInteger));
        return String.valueOf(idToInteger);
    }
}
