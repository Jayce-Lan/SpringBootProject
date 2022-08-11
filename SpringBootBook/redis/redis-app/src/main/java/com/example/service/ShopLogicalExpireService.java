package com.example.service;

import com.example.dto.Result;

public interface ShopLogicalExpireService {
    void saveShop2Redis(Long id, Long expireTime) throws InterruptedException;
    Result queryShopByIdLogicalExpire(Long id);
}
