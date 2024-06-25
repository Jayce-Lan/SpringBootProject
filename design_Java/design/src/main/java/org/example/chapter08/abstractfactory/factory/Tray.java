package org.example.chapter08.abstractfactory.factory;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象零件
 */
public abstract class Tray extends Item {
    protected List<Item> tray = new ArrayList<>();

    public Tray(String caption) {
        super(caption);
    }

    public void add(Item item) {
        tray.add(item);
    }
}
