package org.example.chapter08.abstractfactory.listfactory;

import org.example.chapter08.abstractfactory.factory.Item;
import org.example.chapter08.abstractfactory.factory.Page;

import java.util.Iterator;

public class ListPage extends Page {
    public ListPage(String title, String author) {
        super(title, author);
    }

    @Override
    public String makeHTML() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<html><head><title>" + title + "</title></head>\n<body>\n");
        buffer.append("<h3>" + title + "</h3>");
        buffer.append("<ul>\n");
        Iterator<Item> iterator = content.iterator();
        while (iterator.hasNext()) {
            buffer.append(iterator.next().makeHTML());
        }
        buffer.append("</ul>\n<hr><address>" + author + "</address>");
        buffer.append("</body></html>/n");
        return buffer.toString();
    }
}
