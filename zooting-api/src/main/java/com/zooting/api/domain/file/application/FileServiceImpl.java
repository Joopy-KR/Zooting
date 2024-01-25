package com.zooting.api.domain.file.application;

import com.zooting.api.domain.dm.dao.DMRepository;
import com.zooting.api.domain.dm.entity.DM;
import com.zooting.api.domain.file.dao.FileRepository;
import com.zooting.api.domain.file.dto.response.FileRes;
import com.zooting.api.domain.file.util.S3Util;
import com.zooting.api.global.common.code.ErrorCode;
import com.zooting.api.global.exception.BaseExceptionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService{

    private final S3Util s3Util;
    private final FileRepository fileRepository;
    private final DMRepository dmRepository;
    //TODO thumbnail
    @Override
    public List<FileRes> uploadFiles(List<MultipartFile> multipartFiles) throws IOException {
        return s3Util.uploadFiles(multipartFiles);
    }

    @Override
    public void removeFile(UUID S3Id, Long fileId) {

    }
}
