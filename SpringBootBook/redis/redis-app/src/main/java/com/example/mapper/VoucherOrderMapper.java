package com.example.mapper;

import com.example.entity.SeckillVoucher;
import com.example.entity.VoucherOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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
     * @param seckillVoucher
     * @return
     */
    int updateSeckillVoucherById(SeckillVoucher seckillVoucher);

    /**
     * 新建订单信息
     * @param voucherOrder
     * @return
     */
    int addVoucherOrder(VoucherOrder voucherOrder);

    /**
     * 根据用户id和订单id查询是否存在，用于校验一人一单
     * @param voucherOrder 只需要userId和voucherId
     * @return
     */
    List<VoucherOrder> queryVoucherOrderByUserIdAndVoucherId(VoucherOrder voucherOrder);
}
