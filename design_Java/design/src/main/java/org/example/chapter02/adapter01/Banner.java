package org.example.chapter02.adapter01;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Banner {
    private static final Logger log = LogManager.getLogger(Banner.class);

    private String printMsg;

    public Banner(String printMsg) {
        this.printMsg = printMsg;
    }

    public void showWithParen() {
        log.info("({})", this.printMsg);
    }

    public void showWithAster() {
        log.info("*{}*", this.printMsg);
    }
}
