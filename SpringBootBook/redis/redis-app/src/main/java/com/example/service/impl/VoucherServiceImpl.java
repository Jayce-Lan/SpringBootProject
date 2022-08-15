package com.example.service.impl;

import com.example.dto.Result;
import com.example.entity.SeckillVoucher;
import com.example.entity.Voucher;
import com.example.mapper.VoucherMapper;
import com.example.service.VoucherService;
import com.example.utils.RedisConstants;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service("voucherService")
@Transactional
public class VoucherServiceImpl implements VoucherService {
    @Resource
    VoucherMapper voucherMapper;
    @Resource
    StringRedisTemplate stringRedisTemplate;

    /**
     * 新增普通优惠券信息
     * @param voucher
     * @return
     */
    @Override
    public void addVoucher(Voucher voucher) throws Exception {
        // 普通券
        voucher.setType(0);
        int count = voucherMapper.addVoucher(voucher);
        if (count != 1) {
            throw new Exception("添加条数错误！");
        }
    }

    /**
     * 新增秒杀券
     * 会先新增普通优惠券，然后再新增秒杀券
     * @param voucher
     */
    @Override
    public void addSeckillVoucher(Voucher voucher) throws Exception {
        int count = 0;
        // 秒杀券
        voucher.setType(1);
        count = voucherMapper.addVoucher(voucher);
        if (count != 1) {
            throw new Exception("添加条数错误！");
        }
        // 写入秒杀券
        SeckillVoucher seckillVoucher = new SeckillVoucher();
        seckillVoucher.setVoucherId(voucher.getId());
        seckillVoucher.setStock(voucher.getStock());
        seckillVoucher.setBeginTimeIn(voucher.getBeginTimeIn());
        seckillVoucher.setEndTimeIn(voucher.getEndTimeIn());

        count = voucherMapper.addSeckillVoucher(seckillVoucher);
        if (count != 1) {
            throw new Exception("添加条数错误！");
        }
        // 将秒杀券库存缓存到Redis中
        stringRedisTemplate.opsForValue().set(RedisConstants.SECKILL_STOCK_KEY + voucher.getId(), voucher.getStock().toString());
    }

    @Override
    public Result queryVoucherByShopId(Long shopId) {
        return Result.ok(voucherMapper.queryVoucherByShopId(shopId));
    }
}
