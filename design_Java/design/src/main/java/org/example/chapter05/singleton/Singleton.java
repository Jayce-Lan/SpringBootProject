package org.example.chapter05.singleton;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Singleton {
    private final Logger log = LogManager.getLogger(this.getClass().getName());
    private static Singleton singleton = new Singleton();

    private Singleton() {
        log.info("生成了一个Singleton实例-{}", this.getClass());
    }

    public static Singleton getInstance() {
        return singleton;
    }
}
