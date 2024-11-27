package com.zzang.chongdae.storage.config;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.zzang.chongdae.storage.service.AmazonS3StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {

    @Value("${amazon.s3.bucket}")
    private String bucketName;

    @Value("${amazon.cloudfront.redirectUrl}")
    private String redirectUrl;

    @Bean
    public AmazonS3StorageService storageService() {
        return new AmazonS3StorageService(amazonS3(), bucketName, redirectUrl);
    }

    private AmazonS3 amazonS3() {
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .withRegion(Regions.AP_NORTHEAST_2)
                .build();
    }
}
