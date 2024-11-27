package com.zzang.chongdae.storage.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.storage.exception.StorageErrorCode;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
public class AmazonS3StorageService implements StorageService {

    private final AmazonS3 s3Client;
    private final String bucketName;
    private final String redirectUrl;

    @Override
    public String uploadFile(MultipartFile file, String path) {
        try {
            String objectKey = path + UUID.randomUUID();
            InputStream inputStream = file.getInputStream();
            ObjectMetadata metadata = createMetadata(file);
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectKey, inputStream, metadata);
            s3Client.putObject(putObjectRequest);
            return createUri(objectKey);
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

    private String createUri(String objectKey) {
        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(redirectUrl)
                .path("/" + objectKey)
                .build(false)
                .toString();
    }
}
