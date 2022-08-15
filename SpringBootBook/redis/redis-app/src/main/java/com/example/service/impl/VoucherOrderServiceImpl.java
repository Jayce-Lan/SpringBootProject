package com.example.service.impl;

import com.example.dto.Result;
import com.example.entity.SeckillVoucher;
import com.example.entity.VoucherOrder;
import com.example.mapper.VoucherOrderMapper;
import com.example.service.VoucherOrderService;
import com.example.utils.RedisConstants;
import com.example.utils.RedisIdWorker;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service("voucherOrderService")
@Transactional
public class VoucherOrderServiceImpl implements VoucherOrderService {
    @Resource
    VoucherOrderMapper voucherOrderMapper;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    RedisIdWorker redisIdWorker;

    @Override
    public Result seckillVoucher(Long voucherId) {
        // 查询秒杀券信息
        SeckillVoucher seckillVoucher = voucherOrderMapper.querySeckillVoucherById(voucherId);
        if (seckillVoucher == null) {
            throw new RuntimeException("无优惠券信息！");
        }
        // 查询秒杀是否开始
        if (seckillVoucher.getBeginTime().isAfter(LocalDateTime.now())) {
            throw new RuntimeException("秒杀未开始！");
        }
        // 查询秒杀是否结束
        if (seckillVoucher.getEndTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("秒杀已结束！");
        }
        // 查询库存是否充足
        if (seckillVoucher.getStock() < 1) {
            throw new RuntimeException("库存不足！");
        }
        // 扣件库存
        int count = voucherOrderMapper.updateSeckillVoucherById(voucherId);
        if (count != 1) {
            throw new RuntimeException("库存扣件失败！");
        }

        // 创建订单
        VoucherOrder voucherOrder = new VoucherOrder();
        // 订单id
        long orderId = redisIdWorker.nextId(RedisConstants.ORDER_ID);
        voucherOrder.setId(orderId);
        voucherOrder.setVoucherId(voucherId);
        voucherOrder.setUserId(RedisConstants.IMITATE_USER_ID);
        voucherOrder.setPayType(1);
        voucherOrder.setStatus(1);

        int count1 = voucherOrderMapper.addVoucherOrder(voucherOrder);
        if (count1 != 1) {
            throw new RuntimeException("订单创建失败！");
        }

        return Result.ok("秒杀成功！订单id为：" + orderId);
    }
}
