package com.zzang.chongdae.global.integration;

import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;

import com.zzang.chongdae.global.config.TestConfig;
import com.zzang.chongdae.global.domain.DomainSupplier;
import com.zzang.chongdae.global.helper.CookieProvider;
import com.zzang.chongdae.global.helper.DatabaseCleaner;
import com.zzang.chongdae.member.config.TestNicknameWordPickerConfig;
import com.zzang.chongdae.offering.config.TestCrawlerConfig;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.cookies.RequestCookiesSnippet;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = {TestConfig.class})
@ActiveProfiles("test")
@ExtendWith(RestDocumentationExtension.class)
public abstract class IntegrationTest extends DomainSupplier {

    protected final List<FieldDescriptor> failResponseDescriptors = List.of(
            fieldWithPath("message").description("오류 내용")
    );

    protected final RequestCookiesSnippet requestCookiesSnippet = requestCookies(
            cookieWithName("access_token").description("인증 토큰")
    );

    protected RequestSpecification spec;

    @Autowired
    protected DatabaseCleaner databaseCleaner;

    @Autowired
    protected CookieProvider cookieProvider;

    @LocalServerPort
    private int port;

    protected <E extends Enum<E>> String getEnumValuesAsString(Class<E> enumClass) {
        String enumValues = Arrays.stream(enumClass.getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
        return " (종류: " + enumValues + ")";
    }

    @BeforeEach
    protected void setUp(RestDocumentationContextProvider restDocumentation) {
        RestAssured.port = port;
        spec = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint(), modifyHeaders()
                                .remove("Host")
                                .remove("Content-Length")
                        )
                        .withResponseDefaults(prettyPrint(), modifyHeaders()
                                .remove("Transfer-Encoding")
                                .remove("Keep-Alive")
                                .remove("Date")
                                .remove("Connection")
                                .remove("Content-Length")
                        )
                )
                .build();
        databaseCleaner.execute();
    }
}
