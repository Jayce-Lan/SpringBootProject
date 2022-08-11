package com.example;

import com.alibaba.fastjson.JSONObject;
import com.example.entity.Shop;
import com.example.service.ShopLogicalExpireService;
import com.example.service.ShopService;
import com.example.utils.RedisConstants;
import com.example.utils.RedisData;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@SpringBootTest
@Slf4j
public class ShopApplicationTests {
    @Resource
    private ShopLogicalExpireService shopLogicalExpireService;

    @Resource
    StringRedisTemplate stringRedisTemplate;



    @Test
    void testSaveShop2Redis() throws InterruptedException {
        shopLogicalExpireService.saveShop2Redis(1L, 10L);
    }

    @Test
    void testTime() {
        String json = stringRedisTemplate.opsForValue().get(RedisConstants.CACHE_SHOP_KEY + 1);
        RedisData redisData = JSONObject.parseObject(json, RedisData.class);
        boolean afterFalg = redisData.getExpireTime().isAfter(LocalDateTime.now());
        Shop shop = JSONObject.parseObject(JSONObject.toJSONString(redisData.getData()), Shop.class);
        log.info("shop: {}", shop);
        log.info("falge: {}", afterFalg);
    }
}
