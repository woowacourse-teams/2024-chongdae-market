package com.zzang.chongdae.storage.service;

import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.storage.exception.StorageErrorCode;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
public class LocalStorageService implements StorageService {

    private static final Set<String> ALLOW_IMAGE_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif", "bmp", "svg");

    @Value("${storage.redirectUrl}")
    private String redirectUrl;

    @Value("${storage.path}")
    private String storagePath;

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            String extension = getFileExtension(file);
            validateFileExtension(extension);
            String newFilename = UUID.randomUUID() + "." + extension;
            Path uploadPath = Paths.get(storagePath);
            Path filePath = uploadPath.resolve(newFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return createUri(filePath.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private String getFileExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new MarketException(StorageErrorCode.INVALID_FILE);
        }
        return originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase();
    }

    private void validateFileExtension(String extension) {
        if (!ALLOW_IMAGE_EXTENSIONS.contains(extension)) {
            throw new MarketException(StorageErrorCode.INVALID_FILE_EXTENSION);
        }
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
