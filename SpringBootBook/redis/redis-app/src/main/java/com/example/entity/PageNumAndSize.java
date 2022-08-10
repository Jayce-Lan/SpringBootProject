package com.example.entity;

import lombok.Data;

/**
 * 管理分页参数
 */
@Data
public class PageNumAndSize {
    private Integer pageNum;
    private Integer pageSize;
}
