package com.example.mapper;

import com.example.entity.SeckillVoucher;
import com.example.entity.Voucher;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 优惠券和秒杀券的Mapper
 */
@Mapper
public interface VoucherMapper {
    List<Voucher> queryVoucherByShopId(Long shopId);
    int addVoucher(Voucher voucher);
    int addSeckillVoucher(SeckillVoucher seckillVoucher);
}
