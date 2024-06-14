package org.example.chapter03.templatemethod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StringDisplay extends AbstractDisplay {
    private final Logger log = LogManager.getLogger(StringDisplay.class);

    private String string;

    public StringDisplay(String string) {
        this.string = string;

    }

    @Override
    public void open() {
        log.info("== string open ==");
    }

    @Override
    public void print() {
        log.info("string print >>>>> {}", this.string);
    }

    @Override
    public void close() {
        log.info("== string close ==");
    }
}
