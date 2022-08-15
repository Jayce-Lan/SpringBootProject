package com.example.mapper;

import com.example.entity.SeckillVoucher;
import com.example.entity.VoucherOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VoucherOrderMapper {
    /**
     * 根据秒杀券id获取秒杀券
     * @param id
     * @return
     */
    SeckillVoucher querySeckillVoucherById(Long id);

    /**
     * 更改库存
     * @param id
     * @return
     */
    int updateSeckillVoucherById(Long id);

    /**
     * 新建订单信息
     * @param voucherOrder
     * @return
     */
    int addVoucherOrder(VoucherOrder voucherOrder);
}
