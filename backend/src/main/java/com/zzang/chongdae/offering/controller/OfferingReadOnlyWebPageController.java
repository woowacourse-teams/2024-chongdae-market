package com.zzang.chongdae.offering.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class OfferingReadOnlyWebPageController {

    @GetMapping("/read-only/web/offerings/{offering-id}")
    String renderOfferingDetail(@PathVariable("offering-id") Long offeringId) {
        return "detail";
    }

    @GetMapping("/read-only/web/offerings")
    String renderOffering() {
        return "index";
    }


    @GetMapping("/read-only/web/deeplink/offerings/{offeringId}")
    public String handleDeepLink(@PathVariable Long offeringId,
                                 HttpServletRequest request) throws IOException {

        String userAgent = request.getHeader("User-Agent");

        if (isMobileDevice(userAgent)) {
            // 모바일 기기인 경우 앱으로 리다이렉트
            return "redirect:chongdaeapp://offerings/%d".formatted(offeringId);
        } else {
            return "redirect:read-only/web/offerings/%d".formatted(offeringId);
        }
    }

    private boolean isMobileDevice(String userAgent) {
        return userAgent != null && userAgent.contains("Android");
    }
}
