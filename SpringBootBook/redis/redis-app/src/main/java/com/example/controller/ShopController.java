package com.example.controller;

import com.example.dto.Result;
import com.example.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 商品的Controller层
 * 知识点：使用缓存存储商品信息
 */
@RestController
@RequestMapping("shop")
@Slf4j
public class ShopController {
    @Resource
    ShopService shopService;

    @RequestMapping("/{id}")
    public Result queryShopById(@PathVariable("id") Long id) {
        log.info("id: {}", id);
        return shopService.queryShopById1(id);
    }
}
