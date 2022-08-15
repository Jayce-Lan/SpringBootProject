package com.example.controller;

import com.example.dto.Result;
import com.example.entity.Voucher;
import com.example.service.VoucherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/voucher")
public class VoucherController {
    @Resource
    VoucherService voucherService;

    /**
     * 新增普通优惠券信息
     * @param voucher
     * @return
     */
    @RequestMapping("add-voucher")
    public Result addVoucher(Voucher voucher) throws Exception {
        voucherService.addVoucher(voucher);
        return Result.ok(voucher.getId());
    }

    /**
     * 新增秒杀券
     * @param voucher
     * @return
     */
    @RequestMapping("seckill")
    public Result addSeckillVoucher(Voucher voucher) throws Exception {
        voucherService.addSeckillVoucher(voucher);
        return Result.ok(voucher.getId());
    }

    @GetMapping("/list/{shopId}")
    public Result queryVoucherByShopId(@PathVariable("shopId") Long shopId) {
        return voucherService.queryVoucherByShopId(shopId);
    }
}
