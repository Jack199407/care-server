package com.care.domain.dto;

import lombok.Data;

@Data
public class CommentCreateRequest {
    private Integer star;
    private String content;
}