package com.care.web.controller;

import com.care.domain.dto.UploadedFileDto;
import com.care.domain.service.FileService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/care/")
@Log4j2
public class FileController {

    @Resource
    private FileService fileService;

    @GetMapping("/list")
    public ResponseEntity<List<UploadedFileDto>> listFiles() {
        return ResponseEntity.ok(fileService.listAllFiles());
    }
}
