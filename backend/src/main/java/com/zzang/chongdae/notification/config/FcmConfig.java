package com.zzang.chongdae.notification.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.notification.exception.NotificationErrorCode;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
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
            URL url = this.getClass().getResource(secretKeyPath);
            if (url == null) {
                throw new MarketException(NotificationErrorCode.CANNOT_FIND_URL);
            }
            File secretKeyFile = new File(url.toURI());
            FileInputStream secretKey = new FileInputStream(secretKeyFile);
            FirebaseApp.initializeApp(fcmOptions(secretKey));
            log.info("성공적으로 FCM 앱을 초기화하였습니다.");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (URISyntaxException e) { // todo
            throw new RuntimeException(e);
        }
    }

    private FirebaseOptions fcmOptions(FileInputStream secretKey) throws IOException {
        return FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(secretKey))
                .build();
    }
}
