package com.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.entity.PageNumAndSize;
import com.example.entity.ShopType;
import com.example.mapper.ShopTypeMapper;
import com.example.service.ShopTypeService;
import com.example.utils.RedisConstants;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("shopTypeService")
@Transactional
public class ShopTypeServiceImpl implements ShopTypeService {
    @Resource
    ShopTypeMapper shopTypeMapper;
    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Override
    public PageInfo<ShopType> queryShopTypeByPage(PageNumAndSize pageNumAndSize) {
        PageInfo<ShopType> shopTypePageInfo = PageHelper.startPage(pageNumAndSize.getPageNum(), pageNumAndSize.getPageSize()).doSelectPageInfo(() ->
                shopTypeMapper.queryShopType());
        return shopTypePageInfo;
    }

    /**
     * 使用缓存存储商品类型
     * @return
     */
    @Override
    public List<ShopType> queryShopTypeInResylt() {
        List<String> shopTypeListInRedis = stringRedisTemplate.opsForList().range(RedisConstants.CACHE_SHOP_TYPE, 0, -1);
        // 如果缓存中存在，直接获取
        if (shopTypeListInRedis.size() > 0) {
            List<ShopType> shopTypeList = new ArrayList<>();
            shopTypeListInRedis.forEach(t -> {
                shopTypeList.add(JSONObject.parseObject(t, ShopType.class));
            });
            return shopTypeList;
        }
        // 如果不存在，查询数据库
        List<ShopType> shopTypeList = shopTypeMapper.queryShopType();
        shopTypeList.forEach(t -> {
            // 使用rightPush，向右边插入
            stringRedisTemplate.opsForList().rightPush(RedisConstants.CACHE_SHOP_TYPE, JSONObject.toJSONString(t));
        });
        return shopTypeList;
    }
}
