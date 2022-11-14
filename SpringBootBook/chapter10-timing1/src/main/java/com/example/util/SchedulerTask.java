package com.example.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@EnableAsync
@Slf4j
public class SchedulerTask {
    @Scheduled(cron = "*/5 * * * * ?")
    @Async
    public void taskCron() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        log.info("线程[1]当前时间为： {}", dateFormat.format(new Date()));
    }

    @Scheduled(fixedRate = 5000)
    @Async
    public void taskCron2() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        log.info("线程[2]当前时间为： {}", dateFormat.format(new Date()));
    }
}
