package com.zooting.api.domain.file.application;


import com.zooting.api.domain.file.dto.response.FileRes;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface FileService {

    List<FileRes> uploadFiles(List<MultipartFile> multipartFiles) throws IOException;

    void removeFile(Long fileId);

    Object[] downloadFile(UUID S3Id) throws IOException;


}
