package com.zzang.chongdae.storage.config;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.zzang.chongdae.storage.service.AmazonS3StorageService;
import com.zzang.chongdae.storage.service.StorageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {

    @Bean
    public StorageService storageService() {
        return new AmazonS3StorageService(amazonS3());
    }

    private AmazonS3 amazonS3() {
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .withRegion(Regions.AP_NORTHEAST_2)
                .build();
    }
}
