package com.cache.controller;

import com.cache.pojo.UserADTO;
import com.cache.service.UserService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @RequestMapping("/queryUsers")
    @Cacheable(value = "users")
    public List<UserADTO> queryUsers() {
        return userService.queryUsers();
    }

    @RequestMapping("/queryUserById")
    @Cacheable(value = "user", key = "#userADTO")
    public UserADTO queryUserById(UserADTO userADTO) {
        return userService.queryUserById(userADTO);
    }

    @RequestMapping("/addUser")
    public String addUser(UserADTO userADTO) throws Exception {
        return userService.addUser(userADTO);
    }

    /**
     * 清除缓存方法
     * 清除 value 为users的缓存，如果allEntries为true，那么则会清除当前value下的所有缓存
     * @return 返回处理成功结果
     */
    @RequestMapping("/deleteUsersCache")
    @CacheEvict(value = "users", allEntries = true)
    public String deleteUsersCache() {
        return "缓存清理成功！";
    }
}
