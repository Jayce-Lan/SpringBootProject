package com.example.controller;

import com.example.dto.Result;
import com.example.entity.PageNumAndSize;
import com.example.entity.ShopType;
import com.example.service.ShopTypeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("shop-type")
public class ShopTypeController {
    @Resource
    ShopTypeService shopTypeService;

    @RequestMapping("query-shop-type2")
    public Result queryShopType(PageNumAndSize pageNumAndSize) {
        return Result.ok(shopTypeService.queryShopTypeByPage(pageNumAndSize));
    }

    @RequestMapping("query-shop-type")
    public Result queryShopTypeByResult() {
        List<ShopType> shopTypes = shopTypeService.queryShopTypeInResylt();
        return Result.ok(shopTypes);
    }
}
