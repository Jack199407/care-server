package com.care.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileDownloadResult {
    private byte[] data;
    private String realFilename;

}
