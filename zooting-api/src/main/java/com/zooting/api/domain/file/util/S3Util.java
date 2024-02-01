package com.zooting.api.domain.file.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.zooting.api.domain.file.dao.FileRepository;
import com.zooting.api.domain.file.dto.response.FileRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
        if (multipartFiles == null || multipartFiles.isEmpty()) {
            return null;
        }
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
            //이미지일 경우 썸네일 설정
            String thumbnailUrl = null;
            if (multipartFile.getContentType().contains("image")) {
                thumbnailUrl = uploadThumbnail(multipartFile, folderKey, fileName);
            }
            String fileUrl = amazonS3.getUrl(bucket, objectKey).toString();
            s3FileList.add(new FileRes(
                    randomId,
                    originFileName,
                    fileName,
                    fileUrl,
                    folderKey,
                    thumbnailUrl
            ));
        }
        return s3FileList;
    }

    @Transactional
    public void remove(String folderKey) {
        List<S3ObjectSummary> objectSummaries = amazonS3.listObjects(bucket, folderKey).getObjectSummaries();

        // 내부 오브젝트 삭제
        for (S3ObjectSummary summary : objectSummaries) {
            amazonS3.deleteObject(new DeleteObjectRequest(bucket, summary.getKey()));
        }

        // 폴더 삭제
        amazonS3.deleteObject(bucket, folderKey);
    }

    @Transactional
    public String uploadThumbnail(MultipartFile multipartFile, String folderKey, String fileName) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String thumbnailKey = folderKey + "thumbnail_" + fileName;
        //re-size
        Thumbnails.of(multipartFile.getInputStream())
                .size(100, 100)
                .toOutputStream(outputStream);
        //re-size된 파일 byte[] 로 변환
        byte[] thumbnailBytes = outputStream.toByteArray();
        ByteArrayInputStream thumbnailInputStream = new ByteArrayInputStream(thumbnailBytes);
        ObjectMetadata thumbnailObjectMetadata = new ObjectMetadata();
        thumbnailObjectMetadata.setContentLength(thumbnailBytes.length);
        thumbnailObjectMetadata.setContentType(multipartFile.getContentType());
        //업로드
        amazonS3.putObject(new PutObjectRequest(bucket, thumbnailKey, thumbnailInputStream, thumbnailObjectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3.getUrl(bucket, thumbnailKey).toString();
    }

    public byte[] downloadFile(String fileKey) throws IOException {
        // bucket 와 fileDir 을 사용해서 S3 에 있는 객체 - object - 를 가져온다.
        // TODO: fileDataUrl 가 맞는지 체크
        S3Object object = amazonS3.getObject(new GetObjectRequest(bucket, fileKey));

        // object 를 S3ObjectInputStream 형태로 변환한다.
        S3ObjectInputStream objectInputStream = object.getObjectContent();

        // 이후 다시 byte 배열 형태로 변환한다.
        // 아마도 파일 전송을 위해서는 다시 byte[] 즉, binary 로 변환해서 전달해야햐기 때문

        return IOUtils.toByteArray(objectInputStream);
    }

}
