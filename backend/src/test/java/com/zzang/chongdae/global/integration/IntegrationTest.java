package com.zzang.chongdae.global.integration;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;

import com.zzang.chongdae.global.domain.DomainSupplier;
import com.zzang.chongdae.global.helper.DatabaseCleaner;
import com.zzang.chongdae.offering.config.TestCrawlerConfig;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = {TestCrawlerConfig.class})
@ActiveProfiles("test")
@ExtendWith(RestDocumentationExtension.class)
public abstract class IntegrationTest extends DomainSupplier {

    protected RequestSpecification spec;
    @Autowired
    protected DatabaseCleaner databaseCleaner;
    @LocalServerPort
    private int port;

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
