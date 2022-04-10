package com.example;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyStarter {
    private MyStarterProperties myStarterProperties;

    public MyStarter() {}

    public MyStarter(MyStarterProperties myStarterProperties) {
        this.myStarterProperties = myStarterProperties;
    }

    public String print() {
        String arg = myStarterProperties.getParameter();
        log.info("参数：" + arg);
        return arg;
    }
}
