package com.example.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.dto.Result;
import com.example.entity.Shop;
import com.example.mapper.ShopMapper;
import com.example.service.ShopService;
import com.example.utils.RedisConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service("shopService")
@Slf4j
public class ShopServiceImpl implements ShopService {
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    ShopMapper shopMapper;

    /**
     * 不封装方法的实现
     * @param id
     * @return
     */
    @Override
    public Result queryShopById1(Long id) {
        // 查询缓存中的shop
        String shopJson = stringRedisTemplate.opsForValue().get(RedisConstants.CACHE_SHOP_KEY + id);
        // 如果存在，直接返回
        if (StringUtils.hasText(shopJson)) {
            Shop shop = JSONObject.parseObject(shopJson, Shop.class);
            return Result.ok(shop);
        }
        // 如果缓存中不存在，则查库
        Shop shop = shopMapper.queryShopById(id);
        if (shop == null) {
            return Result.fail("店铺不存在！");
        }
        // 如果数据库查出了店铺，那么将其放入缓存，设置30min过期
        stringRedisTemplate.opsForValue().set(RedisConstants.CACHE_SHOP_KEY + id, JSONObject.toJSONString(shop),
                RedisConstants.CACHE_SHOP_TTL, TimeUnit.MINUTES);
        return Result.ok(shop);
    }

    /**
     * 缓存穿透
     * 使用redis承载空值，防止接口被恶意刷新，重复查询数据库
     * 如果查询结果为空，那么将此结果写入redis
     * @param id
     * @return
     */
    @Override
    public Result queryShopById2(Long id) {
        // 查询缓存中的shop
        String shopJson = stringRedisTemplate.opsForValue().get(RedisConstants.CACHE_SHOP_KEY + id);
        // 如果存在，直接返回
        if (StrUtil.isNotBlank(shopJson)) {
            Shop shop = JSONObject.parseObject(shopJson, Shop.class);
            return Result.ok(shop);
        }

        // 如果shopjson为空白，但是又不为null，即存在键值对，说明为空值
        if (shopJson != null) {
            return Result.fail("店铺不存在！");
        }

        // 如果缓存中不存在，则查库
        Shop shop = shopMapper.queryShopById(id);
        // 如果店铺为空，写入缓存，2min过期
        if (shop == null) {
            stringRedisTemplate.opsForValue().set(RedisConstants.CACHE_SHOP_KEY + id, "",
                    RedisConstants.CACHE_NULL_TTL, TimeUnit.MINUTES);
            return Result.fail("店铺不存在！");
        }
        stringRedisTemplate.opsForValue().set(RedisConstants.CACHE_SHOP_KEY + id, JSONObject.toJSONString(shop),
                RedisConstants.CACHE_SHOP_TTL, TimeUnit.MINUTES);
        return Result.ok(shop);
    }

    @Override
    public Result queryShopById3(Long id) {
        // 互斥锁处理缓存击穿
        Shop shop = queryWithMutex(id);
        if (shop == null) {
            Result.fail("店铺不存在!");
        }
        return Result.ok(shop);
    }


    /**
     * 不封装方法的情况下实现更新数据库后更新缓存
     * 更新缓存策略：删除后再添加
     * @param shop
     * @return
     */
    @Override
    public Result updateShopById1(Shop shop) throws Exception {
        Long id = shop.getId();
        if (id == null) {
            return Result.fail("更新失败！id不能为空！");
        }
        // 更新数据库
        int count = shopMapper.updateShopById(shop);
        if (count != 1) {
            throw new Exception("店铺更新条数异常！");
        }
        // 删除缓存信息
        stringRedisTemplate.delete(RedisConstants.CACHE_SHOP_KEY + id);
        return Result.ok("更新成功！");
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

    /**
     * 互斥锁
     * 处理缓存击穿
     * @param id
     * @return
     */
    private Shop queryWithMutex(Long id) {
        // 查询缓存中的shop
        String shopJson = stringRedisTemplate.opsForValue().get(RedisConstants.CACHE_SHOP_KEY + id);
        // 如果存在，直接返回
        if (StrUtil.isNotBlank(shopJson)) {
            Shop shop = JSONObject.parseObject(shopJson, Shop.class);
            return shop;
        }

        // 如果shopjson为空白，但是又不为null，即存在键值对，说明为空值
        if (shopJson != null) {
            return null;
        }

        // 实现缓存重建
        Shop shop = null;
        try {
            // 1、获取互斥锁
            boolean flag = tryLock(RedisConstants.LOCK_SHOP_KEY + id);
            // 2、判断是否获取成功
            if (!flag) {
                // 3、失败，休眠并重建
                Thread.sleep(50);
                // 重试
                return queryWithMutex(id);
            }

            // 如果缓存中不存在，则查库
            shop = shopMapper.queryShopById(id);
            // 模拟重建延迟
            Thread.sleep(200);

            // 如果店铺为空，写入缓存，2min过期
            if (shop == null) {
                stringRedisTemplate.opsForValue().set(RedisConstants.CACHE_SHOP_KEY + id, "",
                        RedisConstants.CACHE_NULL_TTL, TimeUnit.MINUTES);
                return null;
            }
            stringRedisTemplate.opsForValue().set(RedisConstants.CACHE_SHOP_KEY + id, JSONObject.toJSONString(shop),
                    RedisConstants.CACHE_SHOP_TTL, TimeUnit.MINUTES);

            // 4、释放互斥锁
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            unLock(RedisConstants.LOCK_SHOP_KEY + id);
        }

        return shop;
    }
}
