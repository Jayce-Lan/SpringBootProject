package com.example.service.impl;

import com.example.dto.Result;
import com.example.entity.SeckillVoucher;
import com.example.entity.VoucherOrder;
import com.example.mapper.VoucherOrderMapper;
import com.example.service.VoucherOrderService;
import com.example.utils.RedisConstants;
import com.example.utils.RedisIdWorker;
import com.example.utils.SimpleRedisLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service("voucherOrderService")
public class VoucherOrderServiceImpl implements VoucherOrderService {
    @Resource
    private VoucherOrderMapper voucherOrderMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    // 这里的 RedissonClient 是基于 RedisConfig配置好的Redisson
    @Resource
    private RedissonClient redissonClient;

    @Resource
    private RedisIdWorker redisIdWorker;

    /**
     * 超卖问题
     * <p>
     * 基于秒杀业务实现流程
     * 该方法为直接查库，会存在超卖问题
     *
     * @param voucherId
     * @return
     */
    @Override
    public Result seckillVoucher(Long voucherId) {
        // 查询秒杀券信息
        SeckillVoucher seckillVoucher = voucherOrderMapper.querySeckillVoucherById(voucherId);
        if (seckillVoucher == null) {
            return Result.fail("无优惠券信息");
        }
        // 查询秒杀是否开始
        if (seckillVoucher.getBeginTime().isAfter(LocalDateTime.now())) {
            return Result.fail("秒杀未开始");
        }
        // 查询秒杀是否结束
        if (seckillVoucher.getEndTime().isBefore(LocalDateTime.now())) {
            return Result.fail("秒杀已结束");
        }
        // 查询库存是否充足
        if (seckillVoucher.getStock() < 1) {
            return Result.fail("库存不足!");
        }
        Long userId = RedisConstants.IMITATE_USER_ID;
        synchronized (userId.toString().intern()) {
            // 使用代理对象防止下方方法事务失效
            VoucherOrderService proxy = (VoucherOrderService) AopContext.currentProxy();
            return proxy.createVoucher(voucherId, seckillVoucher);
        }
    }

    /**
     * 将事务方法单独提取出来
     * @param voucherId
     * @param seckillVoucher
     * @return
     */
    @Override
    @Transactional
    public Result createVoucher(Long voucherId, SeckillVoucher seckillVoucher) {
        Long userId = RedisConstants.IMITATE_USER_ID;
        // 创建订单
        VoucherOrder voucherOrder = new VoucherOrder();
        // 订单id
        long orderId = redisIdWorker.nextId(RedisConstants.ORDER_ID);
        voucherOrder.setId(orderId);
        voucherOrder.setVoucherId(voucherId);
        voucherOrder.setUserId(userId);

        // 一人一单
        List<VoucherOrder> voucherOrders = voucherOrderMapper.queryVoucherOrderByUserIdAndVoucherId(voucherOrder);
        if (voucherOrders.size() > 0) {
            return Result.fail("用户已经参与过秒杀！");
        }
        // 扣件库存
        int count = voucherOrderMapper.updateSeckillVoucherById(seckillVoucher);
        if (count != 1) {
            throw new RuntimeException("库存扣件失败！");
        }

        int count1 = voucherOrderMapper.addVoucherOrder(voucherOrder);
        if (count1 != 1) {
            throw new RuntimeException("订单创建失败！");
        }

        return Result.ok("秒杀成功！订单id为：" + orderId);
    }

    /**
     * 使用Redis分布式锁来解决一人一单的问题
     * @param voucherId
     * @return
     */
    @Override
    public Result seckillVoucherByRedisLock(Long voucherId) {
        // 查询秒杀券信息
        SeckillVoucher seckillVoucher = voucherOrderMapper.querySeckillVoucherById(voucherId);
        if (seckillVoucher == null) {
            return Result.fail("无优惠券信息");
        }
        // 查询秒杀是否开始
        if (seckillVoucher.getBeginTime().isAfter(LocalDateTime.now())) {
            return Result.fail("秒杀未开始");
        }
        // 查询秒杀是否结束
        if (seckillVoucher.getEndTime().isBefore(LocalDateTime.now())) {
            return Result.fail("秒杀已结束");
        }
        // 查询库存是否充足
        if (seckillVoucher.getStock() < 1) {
            return Result.fail("库存不足!");
        }
        Long userId = RedisConstants.IMITATE_USER_ID;

        // 创建锁对象
        SimpleRedisLock lock = new SimpleRedisLock(stringRedisTemplate, "order:" + userId);
        // 尝试获取锁
        boolean isLock = lock.tryLock(1200);
        // 判断是否获取锁成功
        if (!isLock) {
            //  获取失败，返回失败
            return Result.fail("不允许重复下单");
        }
        try {
            // 使用代理对象防止下方方法事务失效
            VoucherOrderService proxy = (VoucherOrderService) AopContext.currentProxy();
            return proxy.createVoucher(voucherId, seckillVoucher);
        } finally {
            // 释放锁
            lock.unLock();
        }
    }

    /**
     * 使用Redis提供的Redisson实现分布式锁
     * @param voucherId
     * @return
     */
    @Override
    public Result seckillVoucherByRedisson(Long voucherId) throws InterruptedException {
        // 查询秒杀券信息
        SeckillVoucher seckillVoucher = voucherOrderMapper.querySeckillVoucherById(voucherId);
        if (seckillVoucher == null) {
            return Result.fail("无优惠券信息");
        }
        // 查询秒杀是否开始
        if (seckillVoucher.getBeginTime().isAfter(LocalDateTime.now())) {
            return Result.fail("秒杀未开始");
        }
        // 查询秒杀是否结束
        if (seckillVoucher.getEndTime().isBefore(LocalDateTime.now())) {
            return Result.fail("秒杀已结束");
        }
        // 查询库存是否充足
        if (seckillVoucher.getStock() < 1) {
            return Result.fail("库存不足!");
        }
        Long userId = RedisConstants.IMITATE_USER_ID;

        // 创建锁对象
        RLock lock = redissonClient.getLock("lock:order:" + userId);
        // 获取锁
//        boolean isLock = lock.tryLock(1, 10, TimeUnit.SECONDS);
        // 默认不重复调用，调用失败即返回失败
        boolean isLock = lock.tryLock();
        // 判断是否获取锁成功
        if (!isLock) {
            //  获取失败，返回失败
            return Result.fail("不允许重复下单");
        }
        try {
            // 使用代理对象防止下方方法事务失效
            VoucherOrderService proxy = (VoucherOrderService) AopContext.currentProxy();
            return proxy.createVoucher(voucherId, seckillVoucher);
        } finally {
            // 释放锁
            lock.unlock();
        }
    }
}
