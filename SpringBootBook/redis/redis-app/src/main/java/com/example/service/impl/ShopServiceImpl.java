package com.example.service.impl;

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
}
