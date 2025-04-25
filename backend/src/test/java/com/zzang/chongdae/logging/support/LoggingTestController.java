package com.zzang.chongdae.logging.support;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class LoggingTestController {

    @GetMapping("/read-only/error/internal")
    ResponseEntity<Void> internalError() {
        throw new RuntimeException("internal Server Error");
    }

    @GetMapping("/test")
    ResponseEntity<Void> test() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth/test/secret")
    ResponseEntity<Void> authSecret(@RequestBody SecretRequest request) {
        return ResponseEntity.ok().build();
    }
}
