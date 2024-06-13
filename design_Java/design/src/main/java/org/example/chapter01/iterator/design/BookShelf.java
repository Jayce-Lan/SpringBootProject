package org.example.chapter01.iterator.design;

import org.example.entity.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * 书架的类
 * 由于需要将该类作为集合进行处理，因此实现了Aggregate接口
 */
public class BookShelf implements Aggregate {
    /**
     * 书籍目录
     */
    private List<Book> books;
//    private Book[] books;
    /**
     * 书籍总数
     */
    private int last = 0;

    public BookShelf(int maxSize) {
        this.books = new ArrayList<Book>(maxSize);
//        this.books = new Book[maxSize];
    }

    public Book getBookAt(int index) {
        return books.get(index);
//        return books[index];
    }

    public void appendBook(Book book) {
        this.books.add(book);
//        this.books[last] = book;
        last++;
    }

    public int getLength() {
        return last;
    }

    @Override
    public Iterator iterator() {
        return new BookShelfIterator(this);
    }
}
