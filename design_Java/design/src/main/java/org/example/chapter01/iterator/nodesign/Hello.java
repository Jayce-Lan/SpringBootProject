package org.example.chapter01.iterator.nodesign;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 通常的便利
 */
public class Hello {
    private static final Logger log = LogManager.getLogger(Hello.class);

    public static void main(String[] args) {
        String[] books = {"book1", "book2", "book3"};

        for (int i = 0; i < books.length; i++) {
            log.info("item >>>>> {}", books[i]);
        }
    }
}
