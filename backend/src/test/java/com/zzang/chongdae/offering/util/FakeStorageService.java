package com.zzang.chongdae.offering.util;

import com.zzang.chongdae.storage.service.StorageService;
import org.springframework.web.multipart.MultipartFile;

public class FakeStorageService implements StorageService {

    @Override
    public String uploadFile(MultipartFile file) {
        return "https://upload-image-url.com/";
    }
}
