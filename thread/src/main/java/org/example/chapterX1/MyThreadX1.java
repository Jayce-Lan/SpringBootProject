package org.example.chapterX1;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MyThreadX1 extends Thread {
    private final Logger log = LogManager.getLogger();
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            log.error("Nice {}", i);
        }
    }
}
