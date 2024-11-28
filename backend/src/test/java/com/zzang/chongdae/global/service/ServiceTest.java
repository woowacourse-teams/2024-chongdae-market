package com.zzang.chongdae.global.service;

import com.zzang.chongdae.global.config.TestConfig;
import com.zzang.chongdae.global.domain.DomainSupplier;
import com.zzang.chongdae.global.helper.CookieProvider;
import com.zzang.chongdae.global.helper.DatabaseCleaner;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = {TestConfig.class})
@ActiveProfiles("test")
public abstract class ServiceTest extends DomainSupplier {

    @Autowired
    protected DatabaseCleaner databaseCleaner;

    @Autowired
    protected CookieProvider cookieProvider;

    @BeforeEach
    protected void setUp() {
        databaseCleaner.execute();
    }
}
