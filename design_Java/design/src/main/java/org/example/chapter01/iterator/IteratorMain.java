package org.example.chapter01.iterator;

import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.chapter01.iterator.design.BookShelf;
import org.example.chapter01.iterator.design.Iterator;
import org.example.entity.Book;

public class IteratorMain {
    private static final Logger log = LogManager.getLogger(IteratorMain.class);

    public static void main(String[] args) {
        IteratorMain iteratorMain = new IteratorMain();
        iteratorMain.testIterator();
    }

    private void testIterator() {
        BookShelf bookShelf = new BookShelf(3);
        bookShelf.appendBook(new Book("环游世界80天"));
        bookShelf.appendBook(new Book("灰姑娘"));
        bookShelf.appendBook(new Book("使女的故事"));
        bookShelf.appendBook(new Book("三国演义"));
        log.info("book's length >>>>> {}", bookShelf.getLength());
        Iterator iterator = bookShelf.iterator();
        while (iterator.hasNext()) {
            log.info(JSONObject.toJSONString(iterator.next()));
        }
    }
}
