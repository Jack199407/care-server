package com.care.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadedFileDto {
    private Long fileNumber;
    private String originalName;
}
