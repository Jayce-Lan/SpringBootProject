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
     * @return success-进行扣件并成功；fail-件数为0，扣件失败
     */
    @RequestMapping(path = "/test", method = RequestMethod.GET)
    public String test() {
        String num = stringRedisTemplate.opsForValue().get("num");
        Integer integer = Integer.valueOf(num);
        if (integer > 0) {
            log.info("事务执行成功，执行后余量 >>>>>>> {}", integer);
            integer--;
            stringRedisTemplate.opsForValue().set("num", String.valueOf(integer));
            return "success";
        }
        log.info("事务执行失败！");
        return "fail";
    }
}
