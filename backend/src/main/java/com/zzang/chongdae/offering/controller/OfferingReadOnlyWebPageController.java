package com.zzang.chongdae.offering.controller;

import com.zzang.chongdae.offering.service.OfferingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class OfferingReadOnlyWebPageController {

    OfferingService offeringService;

    @GetMapping("/read-only/web/offerings/{offering-id}")
    String renderOfferingDetail(@PathVariable("offering-id") Long offeringId) {
        return "detail";
    }

    @GetMapping("/read-only/web/offerings")
    String renderOffering() {
        return "index";
    }
    
    @GetMapping("/read-only/deeplink/offerings/{offeringId}")
    public String handleDeepLink(@PathVariable Long offeringId,
                                 HttpServletRequest request) {
        return offeringService.decideRedirectUrl(offeringId, request);
    }
}
