package org.example.chapter03.templatemethod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CharDisplay extends AbstractDisplay {
    private final Logger log = LogManager.getLogger(CharDisplay.class);

    private char aChar;

    public CharDisplay(char aChar) {
        this.aChar = aChar;
    }

    @Override
    public void open() {
        log.info("== char open ==");
    }

    @Override
    public void print() {
        log.info("char >>>>> {}", this.aChar);
    }

    @Override
    public void close() {
        log.info("== char close ==");
    }
}
