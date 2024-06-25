package org.example.chapter08.abstractfactory.listfactory;

import org.example.chapter08.abstractfactory.factory.Factory;
import org.example.chapter08.abstractfactory.factory.Link;
import org.example.chapter08.abstractfactory.factory.Page;
import org.example.chapter08.abstractfactory.factory.Tray;

public class ListFactory extends Factory {
    @Override
    public Link createLink(String caption, String url) {
        return new ListLink(caption, url);
    }

    @Override
    public Tray createTray(String caption) {
        return null;
    }

    @Override
    public Page createPage(String title, String author) {
        return null;
    }
}
