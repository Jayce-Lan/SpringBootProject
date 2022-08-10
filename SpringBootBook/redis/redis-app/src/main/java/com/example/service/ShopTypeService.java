package com.example.service;

import com.example.entity.PageNumAndSize;
import com.example.entity.ShopType;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ShopTypeService {
    PageInfo<ShopType> queryShopTypeByPage(PageNumAndSize pageNumAndSize);
    List<ShopType> queryShopTypeInResylt();
}
