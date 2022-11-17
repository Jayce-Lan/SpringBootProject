package com.example.service;

import com.example.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 模拟业务层读取数据
 */

@Service("userService")
@Slf4j
public class UserService {
    /**
     * 由于缓存被重写了，相应的value会写进redis当中
     * value中的 "user" 会成为键的开头，
     * 类名+方法名+被查询id会与键开头一起组成键
     * 查询结果为值
     * @param id
     * @return
     */
    @Cacheable(value = "user")
    public User getUserById(String id) {
        User user = new User();
        user.setId(id).setPassword("123456")
                .setAge(20)
                .setUserName("简自豪");
        log.info(">>>>>>>>>> 读取了数据库数据（模拟） <<<<<<<<<<");
        return user;
    }
}
