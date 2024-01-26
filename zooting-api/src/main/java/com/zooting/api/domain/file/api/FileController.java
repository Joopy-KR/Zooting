package com.zooting.api.domain.file.api;

import com.zooting.api.domain.file.application.FileService;
import com.zooting.api.domain.file.dto.request.FileReq;
import com.zooting.api.domain.file.dto.response.FileRes;
import com.zooting.api.global.common.BaseResponse;
import com.zooting.api.global.common.code.SuccessCode;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("api/file")
@RequiredArgsConstructor
@Tag(name="파일", description = "파일 관련 API")
public class FileController {
    private final FileService fileService;

    @Transactional
    @PostMapping("/upload")
    public ResponseEntity<BaseResponse<List<FileRes>>> uploadFile(List<MultipartFile> files) throws IOException {
        List<FileRes> fileResList = fileService.uploadFiles(files);
        return BaseResponse.success(
                SuccessCode.INSERT_SUCCESS,
                fileResList
        );
    }

    @Transactional
    @DeleteMapping("/delete")
    public ResponseEntity<BaseResponse<String>> deleteFile(@RequestBody FileReq fileReq) {
        fileService.removeFile(fileReq.fileName(), fileReq.fileDir());
        return BaseResponse.success(
                SuccessCode.DELETE_SUCCESS,
                "파일 삭제 성공"
        );
    }
}
