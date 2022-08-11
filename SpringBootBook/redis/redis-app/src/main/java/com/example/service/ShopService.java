package com.example.service;

import com.example.dto.Result;
import com.example.entity.Shop;

public interface ShopService {
    Result queryShopById1(Long id);
    Result updateShopById1(Shop shop) throws Exception;
}
