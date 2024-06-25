package org.example.chapter08.abstractfactory.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * 抽象产品
 */
public abstract class Page {
    private final Logger log = LogManager.getLogger();
    protected String title;
    protected String author;
    protected List<Item> content = new ArrayList<>();

    public Page(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public void add(Item item) {
        content.add(item);
    }

    public void output() {
        try {
            String fileName = "/Users/lanjiesi/Documents/test/" + title + ".html";
            Writer writer = new FileWriter(fileName);
            writer.write(this.makeHTML());
            writer.close();
        } catch (IOException e) {
            log.error("Page IOException! {}", e.getMessage());
        }
    }

    public abstract String makeHTML();
}
