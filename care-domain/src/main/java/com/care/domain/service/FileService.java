package com.care.domain.service;

import com.care.domain.dto.FileDownloadResult;
import com.care.domain.dto.UploadedFileDto;
import com.care.infrastructure.repository.model.care.UploadedFiles;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    String uploadFile(String name, MultipartFile file);
    void deleteFile(Long fileNumber);
    FileDownloadResult downloadFile(Long fileNumber);
    UploadedFiles getFileMeta(Long fileNumber);
    List<UploadedFileDto> listAllFiles();
}
