package com.zzang.chongdae;

import com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ChongdaeApplicationTests {

    @Test
    void contextLoads() {
        RestAssuredRestDocumentationWrapper.document();
    }

}
