package com.care.infrastructure.repository.mapper.care;

import com.care.infrastructure.repository.model.care.UploadedFiles;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UploadedFilesMapper {

    void insertBatch(@Param("uploadedFiles") List<UploadedFiles> uploadedFiles);

    UploadedFiles selectByFileNumber(@Param("fileNumber") Long fileNumber);

    void deleteByFileNumber(@Param("fileNumber") Long fileNumber);

    List<UploadedFiles> selectAllFiles();
}