package com.example.testThreadPool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class TranTest2Service {
    /**
     * 发送短信提醒1
     * @PostConstruct 加上该注解使得项目启动时就执行一次该方法
     * @Async("taskExecutor") 代码中的 @Async("taskExecutor") 对应我们自定义线程池中的 @Bean("taskExecutor") ，表示使用我们自定义的线程池
     * @throws InterruptedException
     */
    @PostConstruct
    @Async("taskExecutor")
    public void sendMessage1() throws InterruptedException {
        logMsg(1, "开始");
        Thread.sleep(5000);
        logMsg(1, "结束");
    }

    /**
     * 发送短信方法2
     * @throws InterruptedException
     */
    @PostConstruct
    @Async("taskExecutor")
    public void sendMessage2() throws InterruptedException {
        logMsg(2, "开始");
        Thread.sleep(2000);
        logMsg(2, "结束");
    }

    public void logMsg(Integer num, String status) {
        log.info("发送短信方法 >>>>>> {}, {}执行", num, status);
    }
}
