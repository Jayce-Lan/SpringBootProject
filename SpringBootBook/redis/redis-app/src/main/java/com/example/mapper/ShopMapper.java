package com.example.mapper;

import com.example.entity.Shop;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShopMapper {
    Shop queryShopById(Long id);
}
