package com.example.controller;

import com.example.dto.Result;
import com.example.service.VoucherOrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用于秒杀的Controller层
 */

@RestController
@RequestMapping("voucher-order")
public class VoucherOrderController {
    @Resource
    VoucherOrderService voucherOrderService;

    @GetMapping("seckill/{id}")
    public Result seckillVoucher(@PathVariable("id") Long voucherId) throws InterruptedException {
        return voucherOrderService.seckillVoucherByRedisson(voucherId);
    }
}
