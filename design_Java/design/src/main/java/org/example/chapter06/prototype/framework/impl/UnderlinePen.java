package org.example.chapter06.prototype.framework.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.chapter06.prototype.framework.AbstractProduct;

import java.nio.charset.StandardCharsets;

public class UnderlinePen extends AbstractProduct {
    private final Logger log = LogManager.getLogger(this.getClass().getName());
    private char ulchar;

    public UnderlinePen(char ulchar) {
        this.ulchar = ulchar;
    }

    @Override
    public void use(String str) {
        int length = str.getBytes(StandardCharsets.UTF_8).length;
        log.info("\"" + str + "\"");
        StringBuilder foot = new StringBuilder();
        for (int i = 0; i < length; i++) {
            foot.append(ulchar);
        }
        log.info(foot.toString());
    }
}
