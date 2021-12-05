package com.springioc.service;

import com.springioc.pojo.UseProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DataSource4UserProperties {
    @Autowired
    private UseProperties useProperties;

    public void logDatabase() {
        log.info(useProperties.getDriverName());
        log.info(useProperties.getUrl());
        log.info(useProperties.getUsername());
        log.info(useProperties.getPassword());
    }
}
