package com.care.domain.service.impl;

import com.care.domain.dto.FileDownloadResult;
import com.care.domain.dto.UploadedFileDto;
import com.care.domain.service.FileService;
import com.care.infrastructure.repository.mapper.care.UploadedFilesBizMapper;
import com.care.infrastructure.repository.model.care.UploadedFiles;
import com.care.infrastructure.utils.S3Uploader;
import com.care.infrastructure.utils.SnowFlakeIDGenerator;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class FileServiceImpl implements FileService {

    @Resource
    private S3Uploader s3Uploader;
    @Resource
    private UploadedFilesBizMapper uploadedFilesBizMapper;
    @Transactional(rollbackFor = Exception.class, transactionManager = "careTransactionManager")
    @Override
    public String uploadFile(String name, MultipartFile file) {
        String s3Key = null;
        String s3Url;

        try {
            // upload file to S3
            s3Url = s3Uploader.upload(file);
            s3Key = s3Uploader.extractKeyFromUrl(s3Url);

            // insert into db
            UploadedFiles uploadedFiles = generateUploadedFiles(name, SnowFlakeIDGenerator.nextNumber(), s3Url);
            uploadedFilesBizMapper.insertBatch(Collections.singletonList(uploadedFiles));

            return s3Url;
        } catch (Exception e) {
            log.error("File upload failed", e);

            // upload file succeed but database fail, rollback and delete S3 file
            if (s3Key != null) {
                try {
                    s3Uploader.deleteFile(s3Key);
                    log.info("Rolled back uploaded file from S3: {}", s3Key);
                } catch (Exception ex) {
                    log.warn("Failed to delete S3 object after DB insert failure", ex);
                }
            }

            throw new RuntimeException("Upload failed", e); // trigger rollback
        }
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = "careTransactionManager")
    @Override
    public void deleteFile(Long fileNumber) {
        // 1. query if the file exist
        UploadedFiles file = uploadedFilesBizMapper.selectByFileNumber(fileNumber);
        if (file == null) {
            throw new RuntimeException("File not found: " + fileNumber);
        }

        String s3Key = s3Uploader.extractKeyFromUrl(file.getS3Url());

        try {
            // 2. delete S3 文件
            s3Uploader.deleteFile(s3Key);
            log.info("S3 file deleted: {}", s3Key);

            // 3. delete db
            uploadedFilesBizMapper.deleteByFileNumber(fileNumber);

        } catch (Exception e) {
            log.error("Failed to delete file from S3 or DB", e);
            throw new RuntimeException("Delete failed", e); // trigger rollback
        }
    }

    @Override
    public FileDownloadResult downloadFile(Long fileNumber) {
        UploadedFiles file = uploadedFilesBizMapper.selectByFileNumber(fileNumber);
        if (file == null || file.getS3Url() == null) {
            throw new RuntimeException("File not found in DB");
        }

        String key = s3Uploader.extractKeyFromUrl(file.getS3Url());
        byte[] bytes = s3Uploader.downloadFile(key);
        String realFilename = key.substring(key.lastIndexOf("-") + 1);

        return new FileDownloadResult(bytes, realFilename);
    }
    @Override
    public List<UploadedFileDto> listAllFiles() {
        List<UploadedFiles> files = uploadedFilesBizMapper.selectAllFiles();

        return files.stream().map(f -> new UploadedFileDto(f.getFileNumber().toString(),
                f.getOriginalName())).collect(Collectors.toList());
    }

    @Override
    public UploadedFiles getFileMeta(Long fileNumber) {
        return uploadedFilesBizMapper.selectByFileNumber(fileNumber);
    }

    private UploadedFiles generateUploadedFiles(String name, Long fileNumber, String url) {
        UploadedFiles files = new UploadedFiles();
        files.setFileNumber(fileNumber);
        files.setOriginalName(name);
        files.setS3Url(url);
        return files;
    }
}
