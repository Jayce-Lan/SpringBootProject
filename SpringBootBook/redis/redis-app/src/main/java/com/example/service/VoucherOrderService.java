package com.example.service;

import com.example.dto.Result;
import com.example.entity.SeckillVoucher;

public interface VoucherOrderService {
    /**
     * 使用Java线程锁解决一人一单问题
     * @param voucherId
     * @return
     */
    Result seckillVoucher(Long voucherId);
    Result createVoucher(Long voucherId, SeckillVoucher seckillVoucher);

    /**
     * 使用 Redis分布式锁解决一人一单问题
     * @param voucherId
     * @return
     */
    Result seckillVoucherByRedisLock(Long voucherId);

    Result seckillVoucherByRedisson(Long voucherId) throws InterruptedException;
}
