package com.example.service;

import com.example.dto.Result;
import com.example.entity.Voucher;

/**
 * 秒杀券和优惠券的Service
 */
public interface VoucherService {
    void addVoucher(Voucher voucher) throws Exception;
    void addSeckillVoucher(Voucher voucher) throws Exception;
    Result queryVoucherByShopId(Long shopId);
}
