package org.example.chapter01.iterator.design;

import org.example.entity.Book;

/**
 * 书架迭代器
 */
public class BookShelfIterator implements Iterator {
    private BookShelf bookShelf;
    private Integer index;

    public BookShelfIterator(BookShelf bookShelf) {
        this.bookShelf = bookShelf;
        this.index = 0;
    }

    @Override
    public boolean hasNext() {
        return index < bookShelf.getLength();
    }

    @Override
    public Object next() {
        Book book = bookShelf.getBookAt(this.index);
        this.index++;
        return book;
    }
}
