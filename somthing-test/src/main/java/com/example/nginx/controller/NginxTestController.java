package com.example.nginx.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/nginxTestController")
@Slf4j
public class NginxTestController {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 测试nginx执行
     */
    @RequestMapping(path = "/test", method = RequestMethod.GET)
    public void test() {
        String num = stringRedisTemplate.opsForValue().get("num");
        Integer integer = Integer.valueOf(num);
        log.info("测试成功 >>>>>>>>> {}", num);
        integer--;
        stringRedisTemplate.opsForValue().set("num", String.valueOf(integer));
    }
}
