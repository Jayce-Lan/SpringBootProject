package com.elasticsearch.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;

@Data
@Document(indexName = "book")
@Accessors(chain = true)
public class Book {
    @Id
    private Long id;
    @Field(analyzer = "ik_max_word",type = FieldType.Text)
    private String bookName;
    @Field(type = FieldType.Keyword)
    private String author;
    private BigDecimal price;
    private int page;
    @Field(type = FieldType.Keyword, fielddata = true)
    private String category;

    public Book() {
    }

    public Book(Long id, String bookName, String author, BigDecimal price, int page, String category) {
        this.id = id;
        this.bookName = bookName;
        this.author = author;
        this.price = price;
        this.page = page;
        this.category = category;
    }
}
