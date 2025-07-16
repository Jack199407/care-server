package com.care.web.controller.admin;

import com.care.domain.dto.UploadedFileDto;
import com.care.domain.service.FileService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/admin/care")
@Log4j2
public class AdminFileController {

    @Resource
    private FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("name") String name,
                                             @RequestParam("file") MultipartFile file) {
        try {
            String url = fileService.uploadFile(name, file);
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            log.error("File upload failed", e);
            return ResponseEntity.internalServerError().body("Upload failed: " + e.getMessage());
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam("fileNumber") Long fileNumber) {
        try {
            fileService.deleteFile(fileNumber);
            return ResponseEntity.ok("File deleted successfully");
        } catch (Exception e) {
            log.error("File deletion failed", e);
            return ResponseEntity.internalServerError().body("Delete failed: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<UploadedFileDto>> listFiles() {
        return ResponseEntity.ok(fileService.listAllFiles());
    }
}
