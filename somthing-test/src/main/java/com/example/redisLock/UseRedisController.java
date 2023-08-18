package com.example.redisLock;

import com.example.util.CommonDict;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/useRedisController")
@Slf4j
public class UseRedisController {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private Redisson redisson;

    /**
     * 使用Redis SET key value EX seconds  NX 特性实现锁
     * @return 返回执行结果：success-成功；fail-失败
     */
    @RequestMapping(path = "/useRedis", method = RequestMethod.GET)
    public String useRedis() {
        try {
            // 原子的设置key（锁）及超时时间
            Boolean aTrue = stringRedisTemplate.opsForValue().setIfAbsent(CommonDict.REDIS_KEY_LOCK, "true", 30, TimeUnit.SECONDS);
            if (!aTrue) {
                return "fail";
            }
            int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get(CommonDict.REDIS_KEY_NUM));
            if (stock > 0) {
                stock--;
                stringRedisTemplate.opsForValue().set(CommonDict.REDIS_KEY_NUM, String.valueOf(stock));
                log.info("事务执行成功！扣减后剩余 >>>>>> {}", stock);
            } else {
                log.info("事务执行失败！");
            }
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
        } finally {
            // 避免死锁
            stringRedisTemplate.delete(CommonDict.REDIS_KEY_LOCK);
        }
        return "success";
    }

    /**
     * 使用Redisson实现锁，防止事务重复提交
     * @return 返回执行结果：success-成功；fail-失败
     */
    @RequestMapping(path = "/useRedissonLock", method = RequestMethod.GET)
    public String useRedissonLock() {
        RLock rLock = redisson.getLock(CommonDict.REDIS_KEY_LOCK);
        try {
            rLock.lock();
            int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get(CommonDict.REDIS_KEY_NUM));
            if (stock > 0) {
                stock--;
                stringRedisTemplate.opsForValue().set(CommonDict.REDIS_KEY_NUM, String.valueOf(stock));
                log.info("事务执行成功！扣减后剩余 >>>>>> {}", stock);
            } else {
                log.info("事务执行失败！");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            // 防止死锁
            rLock.unlock();
        }
        return "success";
    }
}
