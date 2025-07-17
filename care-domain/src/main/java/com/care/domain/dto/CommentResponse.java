package com.care.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponse {
    private Integer id;
    private Integer star;
    private String content;
    private Boolean display;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private Long deleteAt;
}
