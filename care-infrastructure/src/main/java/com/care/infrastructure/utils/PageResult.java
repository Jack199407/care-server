package com.care.infrastructure.utils;

import lombok.Data;

import java.util.List;
@Data
public class PageResult<T> {
    private List<T> records;
    private long total;
    private int pageNum;
    private int pageSize;
}
