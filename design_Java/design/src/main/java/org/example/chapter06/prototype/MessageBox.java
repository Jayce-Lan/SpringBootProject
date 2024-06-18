package org.example.chapter06.prototype;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.chapter06.prototype.framework.Product;

import java.nio.charset.StandardCharsets;

public class MessageBox implements Product {
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

    @Override
    public Product createClone() {
        Product product = null;
        try {
            product = (Product) clone();
        } catch (CloneNotSupportedException e) {
            log.error(e.getMessage());
        }
        return product;
    }
}
