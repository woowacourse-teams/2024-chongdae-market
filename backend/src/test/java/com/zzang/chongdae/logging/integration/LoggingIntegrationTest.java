package com.zzang.chongdae.logging.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.zzang.chongdae.global.exception.GlobalExceptionHandler;
import com.zzang.chongdae.global.integration.IntegrationTest;
import com.zzang.chongdae.logging.config.LoggingFilter;
import com.zzang.chongdae.logging.config.LoggingInterceptor;
import com.zzang.chongdae.logging.support.InMemoryLogAppender;
import com.zzang.chongdae.logging.support.SecretRequest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

public class LoggingIntegrationTest extends IntegrationTest {

    private InMemoryLogAppender appender;

    @BeforeEach
    void setUp() {
        appender = new InMemoryLogAppender();
        appender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        appender.start();
        Logger errorLogger = (Logger) LoggerFactory.getLogger(GlobalExceptionHandler.class);
        Logger infoLoggerInterceptor = (Logger) LoggerFactory.getLogger(LoggingInterceptor.class);
        Logger infoLoggerFilter = (Logger) LoggerFactory.getLogger(LoggingFilter.class);
        errorLogger.addAppender(appender);
        infoLoggerInterceptor.addAppender(appender);
        infoLoggerFilter.addAppender(appender);
    }

    @AfterEach
    void tearDown() {
        appender.clear();
    }

    @DisplayName("500 오류가 발생할 경우 stackTrace가 기록되어야 한다.")
    @Test
    void should_log_stackStrace_when_500_errorOccurred() {
        given().log().all()
                .when().get("/read-only/error/internal")
                .then().log().all()
                .statusCode(500);
        boolean actual = appender.getLogs().stream()
                .anyMatch(e -> e.getLevel() == Level.ERROR && e.getMessage().contains("internal Server Error"));
        assertThat(actual).isTrue();
    }

    @DisplayName("interceptor 접근권한 오류가 발생해도 로깅이 되어야 한다.")
    @Test
    void should_log_when_accessDeniedErrorOccurred() {
        given().log().all()
                .when().get("/test")
                .then().log().all()
                .statusCode(401);
        boolean actual = appender.getLogs().stream()
                .anyMatch(e -> e.getLevel() == Level.WARN);
        assertThat(actual).isTrue();
    }

    @DisplayName("중요정보는 마스킹되어 로깅해야 한다.")
    @Test
    void should_log_masked_requestBody_when_annotatedMaskedLogging() {
        SecretRequest request = new SecretRequest("secret information");
        given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/auth/test/secret")
                .then().log().all()
                .statusCode(200);
        boolean actual = appender.getLogs().stream()
                .anyMatch(e -> e.getLevel() == Level.INFO && e.getMessage().contains("[MASKED_INFORMATION]"));
        assertThat(actual).isTrue();
    }

    @DisplayName("multipart 요청 시 requestBody는 로깅되지 않아야 한다.")
    @Test
    void should_not_log_requestBody_when_multipartRequest() {
        given().log().all()
                .multiPart("file", "testfile.txt", "dummy content".getBytes())
                .when()
                .post("/test-upload");

        boolean bodyLogged = appender.getLogs().stream()
                .anyMatch(e -> e.getMessage().contains("dummy content"));

        assertThat(bodyLogged).isFalse();
    }
}
