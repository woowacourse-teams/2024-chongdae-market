package com.zzang.chongdae.storage.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.storage.exception.StorageErrorCode;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class StorageService {

    private final AmazonS3 s3Client;

    @Value("${amazon.s3.bucket}")
    private String bucketName;

    public String uploadFile(MultipartFile file, String path) {
        try {
            String objectKey = path + UUID.randomUUID();
            InputStream inputStream = file.getInputStream();
            ObjectMetadata metadata = createMetadata(file);
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectKey, inputStream, metadata);

            PutObjectResult result = s3Client.putObject(putObjectRequest);
            System.out.println(result.getMetadata().toString());
            return s3Client.getUrl(bucketName, objectKey).toString();
        } catch (IOException e) {
            throw new MarketException(StorageErrorCode.INVALID_FILE);
        } catch (SdkClientException e) {
            throw new MarketException(StorageErrorCode.STORAGE_SERVER_FAIL);
        }
    }

    private ObjectMetadata createMetadata(MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());
        return metadata;
    }
}
