package com.care.web.controller.admin;

import com.care.infrastructure.utils.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/care")
@Log4j2
@RequiredArgsConstructor
public class AdminFileController {

    private final S3Uploader s3Uploader;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String url = s3Uploader.upload(file);
            log.info("File uploaded to: {}", url);
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            log.error("File upload failed", e);
            return ResponseEntity.internalServerError().body("Upload failed: " + e.getMessage());
        }
    }
}
