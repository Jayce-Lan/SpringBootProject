package com.elasticsearch.repository;

import com.elasticsearch.entity.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface BookRepository extends ElasticsearchRepository<Book, Integer> {
    List<Book> findByBookNameLike(String bookName);
}
