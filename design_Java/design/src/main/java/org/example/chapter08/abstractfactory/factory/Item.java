package org.example.chapter08.abstractfactory.factory;

/**
 * 抽象零件
 */
public abstract class Item {
    protected String caption;

    public Item(String caption) {
        this.caption = caption;
    }

    public abstract String makeHTML();
}
