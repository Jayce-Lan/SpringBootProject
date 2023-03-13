package com.elasticsearch;

import com.elasticsearch.entity.Book;
import com.elasticsearch.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Lazy;

import javax.annotation.Resource;
import java.math.BigDecimal;

@SpringBootTest
@Slf4j
public class Chapter13EsTestTest {
    @Resource
    private BookRepository bookRepository;

    @Test
    void test() {
        Book book = new Book(1L, "xyj", "wce", new BigDecimal("36.88"), 3000, "gdmz");
        bookRepository.save(book);
        Book book1 = bookRepository.findById(1).orElse(null);
        log.info("book: {}", book1);
    }


}
