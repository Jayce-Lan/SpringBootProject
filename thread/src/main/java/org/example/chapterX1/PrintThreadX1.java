package org.example.chapterX1;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PrintThreadX1 extends Thread {
    private String message;
    private static final Logger log = LogManager.getLogger();

    public PrintThreadX1(String message) {
        this.message = message;
    }

    @Override
    public void run() {
        for (int i = 0; i < 50; i++) {
            log.info("{} {}", this.message, i);
        }
    }
}
