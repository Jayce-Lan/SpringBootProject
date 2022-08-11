package com.example.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.dto.Result;
import com.example.entity.Shop;
import com.example.mapper.ShopMapper;
import com.example.service.ShopLogicalExpireService;
import com.example.utils.RedisConstants;
import com.example.utils.RedisData;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 使用逻辑过期解决缓存击穿问题
 */
@Service("shopLogicalExpireService")
public class ShopLogicalExpireServiceImpl implements ShopLogicalExpireService {
    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    ShopMapper shopMapper;

    // 线程池
    private static final ExecutorService CACHE_EXECUTOR = Executors.newFixedThreadPool(10);

    /**
     * 声明一个方法，设置逻辑过期时间
     * 该方法在正常项目中会有后台系统处理，此处使用单元测试模拟
     *
     * @param id 商铺id
     * @param expireTime 过期时间
     */
    @Override
    public void saveShop2Redis(Long id, Long expireTime) throws InterruptedException {
        Shop shop = shopMapper.queryShopById(id);

        RedisData redisData = new RedisData();
        redisData.setData(shop);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(expireTime));

        stringRedisTemplate.opsForValue().set(RedisConstants.CACHE_SHOP_KEY + id, JSONUtil.toJsonStr(redisData));
    }

    /**
     * 在逻辑过期当作，默认信息在缓存中一定存在
     * 其实逻辑过期中的缓存数据，并没有过期时间，只是对象当中存储了
     *
     * @param id
     * @return
     */
    @Override
    public Result queryShopByIdLogicalExpire(Long id) {
        // 从Redis中查询商铺缓存
        String jsonShop = stringRedisTemplate.opsForValue().get(RedisConstants.CACHE_SHOP_KEY + id);
        // 如果未命中，直接返回
        if (StrUtil.isBlank(jsonShop)) {
            return Result.fail("商铺信息不存在！");
        }
        // 1、命中，先要把json反序列化为对象
        RedisData redisData = JSONUtil.toBean(jsonShop, RedisData.class);
        JSONObject data = (JSONObject) redisData.getData();
        Shop shop = JSONUtil.toBean(data, Shop.class);
        LocalDateTime expireTime = redisData.getExpireTime();
        // 2、判断是否过期
        if (expireTime.isAfter(LocalDateTime.now())) {
            // 2-1、未过期，直接返回店铺信息
            return Result.ok(shop);
        }
        // 2-2、过期，需要缓存重建
        // 3、缓存重建
        // 3-1、获取互斥锁
        String lockKey = RedisConstants.LOCK_SHOP_KEY + id;
        // 3-2、判断是否获取锁成功
        boolean isLock = tryLock(lockKey);
        if (isLock) {
            // 3-3、成功，开启独立线程，实现缓存重建
            CACHE_EXECUTOR.submit(() -> {
                try {
                    // 重建缓存
                    this.saveShop2Redis(id, 20L);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    // 释放锁
                    unLock(lockKey);
                }
            });
        }
        // 3-4、返回过期的商铺信息
        return Result.ok(shop);
    }

    /**
     * 使用互斥锁来解决缓存击穿的问题
     * 获取锁
     * @param key
     * @return
     */
    private boolean tryLock(String key) {
        // 10s过期，使用setnx的特性进行互斥锁
        Boolean flage = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", RedisConstants.LOCK_SHOP_TTL, TimeUnit.SECONDS);
        return BooleanUtil.isTrue(flage);
    }

    /**
     * 释放锁
     * @param key
     */
    private void unLock(String key) {
        stringRedisTemplate.delete(key);
    }
}
