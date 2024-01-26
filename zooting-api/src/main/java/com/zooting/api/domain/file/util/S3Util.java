package com.zooting.api.domain.file.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.zooting.api.domain.file.dao.FileRepository;
import com.zooting.api.domain.file.dto.response.FileRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Log4j2
@Component
@RequiredArgsConstructor
public class S3Util {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;
    private final FileRepository fileRepository;

    // 파일 업로드
    @Transactional
    public List<FileRes> uploadFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<FileRes> s3FileList = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            String originFileName = multipartFile.getOriginalFilename(); // 원본 파일명
            String folderKey = UUID.randomUUID() + "/"; // 폴더 주소
            UUID randomId = UUID.randomUUID();
            String fileName = randomId + "_" + originFileName; // 변환된 파일명

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(multipartFile.getSize());
            objectMetadata.setContentType(multipartFile.getContentType());

            if (!amazonS3.doesObjectExist(bucket, folderKey)) {
                amazonS3.putObject(bucket, folderKey, "");
            }

            String objectKey = folderKey + fileName;
            amazonS3.putObject(new PutObjectRequest(bucket, objectKey, multipartFile.getInputStream(), objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            String fileUrl = amazonS3.getUrl(bucket, objectKey).toString();
            s3FileList.add(new FileRes(
                    randomId,
                    originFileName,
                    fileName,
                    fileUrl
            ));
        }
        return s3FileList;
    }

}
