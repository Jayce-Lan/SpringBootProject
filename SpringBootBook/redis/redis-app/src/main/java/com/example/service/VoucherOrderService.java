package com.example.service;

import com.example.dto.Result;
import com.example.entity.SeckillVoucher;

public interface VoucherOrderService {
    Result seckillVoucher(Long voucherId);
    Result createVoucher(Long voucherId, SeckillVoucher seckillVoucher);
}
