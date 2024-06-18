package org.example.chapter06.prototype.framework.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.chapter06.prototype.framework.AbstractProduct;

import java.nio.charset.StandardCharsets;

public class MessageBox extends AbstractProduct {
    private final Logger log = LogManager.getLogger(this.getClass().getName());
    private char decochar;

    public MessageBox(char decochar) {
        this.decochar = decochar;
    }

    @Override
    public void use(String str) {
        int length = str.getBytes(StandardCharsets.UTF_8).length;
        StringBuilder head = new StringBuilder();
        for (int i = 0; i < length + 4; i++) {
            head.append(decochar);
        }
        log.info(head.toString());
        log.info(decochar + " " + str + " " + decochar);
        log.info(head.toString());
    }
}
