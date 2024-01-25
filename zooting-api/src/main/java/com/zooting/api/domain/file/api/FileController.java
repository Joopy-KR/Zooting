package com.zooting.api.domain.file.api;

import com.zooting.api.domain.file.application.FileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("api/file")
@RequiredArgsConstructor
@Tag(name="파일", description = "파일 관련 API")
public class FileController {
    private final FileService fileService;
}
