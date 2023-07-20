package com.example.cache;

import com.example.entity.UserToCacheDTO;
import com.example.util.BloomFilterUtil;
import com.example.util.CommonDict;
import com.example.util.ValidateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/userToCache")
@Slf4j
public class UserToCacheController {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 解决缓存穿透
     * 缓存穿透指的是一个缓存系统无法缓存某个查询的数据，从而导致这个查询每一次都要访问数据库。
     * 常见的Redis缓存穿透场景包括：
     *
     * 查询一个不存在的数据：攻击者可能会发送一些无效的查询来触发缓存穿透。
     * 查询一些非常热门的数据：如果一个数据被访问的非常频繁，那么可能会导致缓存系统无法处理这些请求，从而造成缓存穿透。
     * 查询一些异常数据：这种情况通常发生在数据服务出现故障或异常时，从而造成缓存系统无法访问相关数据，从而导致缓存穿透。
     *
     * @param id
     * @return
     */
    @GetMapping("/user1/{id}")
    public UserToCacheDTO getUserById(@PathVariable String id) {
        String cacheId = CommonDict.USER_TO_CACHE_KEY + id;
        // 先判定布隆过滤器中是否存在此id
        if (BloomFilterUtil.mightContain(cacheId)) {
            log.info(">>>>>>>>>>>>> 布隆过滤器被执行了！");
            return null;
        }
        // 查询缓存数据
        UserToCacheDTO user = (UserToCacheDTO) redisTemplate.opsForValue().get(cacheId);
        if (ValidateUtil.isEmpty(user)) {
            // 模拟查询数据库
            user = new UserToCacheDTO();
            user.setId(id).setName("Sam").setAge(20);
            if (!ValidateUtil.isEmpty(user)) {
                // 将数据加入缓存
                redisTemplate.opsForValue().set(cacheId, user, 1, TimeUnit.MINUTES);
            } else {
                // 如果查询结果为空，将请求记录，并在布隆过滤器中添加
                BloomFilterUtil.add(cacheId);
            }
        }
        log.info(">>>>>>>>>>>>> 布隆过滤器未执行！");
        return user;
    }

    /**
     * 解决缓存穿透
     * 使用Redis解决缓存穿透
     * 此处不实用布隆过滤器，而是使用Redis创建一个新的缓存对象，当垃圾请求进来时，直接读取内存中的数据
     *
     * 这样的好处是，当我们在缓存存在期间插入的用户id正好为垃圾请求的id，
     * 那么我们之际删除id后插入，这样内存中就不会为null，而是新插入的对象
     *
     * @param id
     * @return
     */
    @GetMapping("/user2/{id}")
    public UserToCacheDTO getUserById2(@PathVariable String id) {
        String cacheId = CommonDict.USER_TO_CACHE_KEY + id;
        // 判定该key在redis中是否存在
        Boolean keyFlag = redisTemplate.hasKey(cacheId);
        log.info("是否存在key >>>>>> {}", keyFlag);
        UserToCacheDTO user;
        // 如果key存在，直接读取缓存值
        if (keyFlag) {
            // 查询缓存数据
            user = (UserToCacheDTO) redisTemplate.opsForValue().get(cacheId);
        } else {
            // key不存在，模拟读数据库
            user = new UserToCacheDTO();
            user.setId(id).setName("Sam").setAge(20);
            // 无论是否存在，都进行写缓存
            redisTemplate.opsForValue().set(cacheId, user, 1, TimeUnit.MINUTES);
        }
        return user;
    }

    /**
     * 缓存击穿
     * 缓存击穿指的是在一些高并发访问下，一个热点数据从缓存中不存在，每次请求都要直接查询数据库，从而导致数据库压力过大，并且系统性能下降的现象。
     * 缓存击穿的原因通常有以下几种：
     *
     * 缓存中不存在所需的热点数据：当系统中某个热点数据需要被频繁访问时，如果这个热点数据最开始没有被缓存，那么就会导致系统每次请求都需要直接查询数据库，造成数据库负担。
     * 缓存的热点数据过期：当一个热点数据过期并需要重新缓存时，如果此时有大量请求，那么就会导致所有请求都要直接查询数据库。
     *
     * 在遇到缓存击穿问题时，我们可以在查询数据库之前，先判断一下缓存中是否已有数据，如果没有数据则使用Redis的单线程特性，先查询数据库然后将数据写入缓存中。
     *
     * @param id
     * @return
     */
    @GetMapping("/userJC1/{id}")
    public UserToCacheDTO getUserByIdJC1(@PathVariable String id) {
        String cacheId = CommonDict.USER_TO_CACHE_KEY + id;
        UserToCacheDTO user = (UserToCacheDTO) redisTemplate.opsForValue().get(cacheId);
        // 如果查询缓存为空，那么进入查库，并给上锁
        if (ValidateUtil.isEmpty(user)) {
            // 查库前上锁
            String lockKey = CommonDict.USER_TO_QUERY_LOCK_KEY + id;
            String lockValue = UUID.randomUUID().toString();
            try {
                Boolean lockResultFlag = redisTemplate.opsForValue().setIfAbsent(lockKey, lockValue, 60, TimeUnit.SECONDS);
                if (Boolean.TRUE.equals(lockResultFlag)) {
                    // 模拟查库
                    user = new UserToCacheDTO();
//                    user.setId(id).setName("Sam").setAge(20);
                    if (!ValidateUtil.isEmpty(user)) {
                        redisTemplate.opsForValue().set(cacheId, user, 1, TimeUnit.MINUTES);
                        log.info(">>>>>>>>>>> 写入缓存的步骤被执行了");
                    }
                }
            } finally {
                // 释放锁
                if (lockValue.equals(redisTemplate.opsForValue().get(lockKey))) {
                    redisTemplate.delete(lockKey);
                }
            }
        }
        return user;
    }
}
