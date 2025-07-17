package com.care.domain.dto;

import lombok.Data;

import java.util.List;
@Data
public class CommentQueryRequest {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private List<Boolean> display;


    public boolean isValid() {
        return display != null && !display.isEmpty();
    }
}
