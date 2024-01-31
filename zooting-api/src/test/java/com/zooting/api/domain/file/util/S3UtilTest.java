package com.zooting.api.domain.file.util;

import com.zooting.api.domain.file.dto.response.FileRes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class S3UtilTest {

    @Autowired
    private S3Util s3Util;

    @Test
    void uploadFiles() throws IOException {
        // Create a list of mock MultipartFile objects
        List<MultipartFile> multipartFiles = new ArrayList<>();
        MockMultipartFile file1 = new MockMultipartFile(
                "file",
                "test_file_1.txt",
                "text/plain",
                "This is a test file content".getBytes()
        );
        MockMultipartFile file2 = new MockMultipartFile(
                "file",
                "test_file_2.txt",
                "text/plain",
                "This is another test file content".getBytes()
        );
        multipartFiles.add(file1);
        multipartFiles.add(file2);

        // Perform the file upload
        List<FileRes> s3FileList = s3Util.uploadFiles(multipartFiles);

        // Assertions
        assertNotNull(s3FileList);
        assertEquals(2, s3FileList.size());

        for (FileRes fileRes : s3FileList) {
            assertNotNull(fileRes.S3Id());
            assertNotNull(fileRes.fileName());
            assertNotNull(fileRes.imgUrl());
        }
    }
}