package com.example.testThreadPool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class OrderTaskService {
    @Resource
    private TranTest2Service tranTest2Service;

    /**
     * 订单处理任务
     */
    public void orderTask() throws InterruptedException {
        this.cancelOrder(); // 取消订单
        tranTest2Service.sendMessage1();
        tranTest2Service.sendMessage2();
    }

    /**
     * 取消订单
     */
    public void cancelOrder() {
        log.info("取消订单的方法执行 >>>>> 开始");
        log.info("取消订单的方法执行 >>>>> 结束");
    }
}
