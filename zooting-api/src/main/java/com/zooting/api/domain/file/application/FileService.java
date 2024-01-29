package com.zooting.api.domain.file.application;


import com.zooting.api.domain.file.dto.response.FileRes;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {

    List<FileRes> uploadFiles(List<MultipartFile> multipartFiles) throws IOException;

    void removeFile(String fileName, String fileDir);

    Object[] downloadFile(String fileName) throws IOException;


}
