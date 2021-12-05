package com.springioc.service;

import com.springioc.pojo.User1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class User1Service {
    public void printUser1(User1 user1) {
        log.error(user1.getId());
        log.error(user1.getName());
        log.error(user1.getNote());
    }
}
