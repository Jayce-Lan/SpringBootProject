package com.example.mapper;

import com.example.entity.ShopType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShopTypeMapper {
    List<ShopType> queryShopType();
}
