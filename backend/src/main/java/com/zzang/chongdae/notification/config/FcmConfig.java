package com.zzang.chongdae.notification.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class FcmConfig {

    @Value("${fcm.secret-key.path}")
    private String secretKeyPath;

    @PostConstruct
    public void initialize() {
        if (!FirebaseApp.getApps().isEmpty()) {
            log.info("성공적으로 FCM 앱을 실행하였습니다.");
            return;
        }
        try {
            InputStream secretKey = this.getClass().getResourceAsStream(secretKeyPath);
            FirebaseApp.initializeApp(fcmOptions(secretKey));
            log.info("성공적으로 FCM 앱을 초기화하였습니다.");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private FirebaseOptions fcmOptions(InputStream secretKey) throws IOException {
        return FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(secretKey))
                .build();
    }
}
