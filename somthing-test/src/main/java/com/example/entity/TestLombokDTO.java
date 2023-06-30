package com.example.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class TestLombokDTO {
    private String name;
    private BigDecimal balance;
}
