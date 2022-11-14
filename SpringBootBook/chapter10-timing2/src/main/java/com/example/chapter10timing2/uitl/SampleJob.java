package com.example.chapter10timing2.uitl;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

@Slf4j
@Setter
public class SampleJob extends QuartzJobBean {
    private String name;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("jobName: {}", this.name);
    }
}
