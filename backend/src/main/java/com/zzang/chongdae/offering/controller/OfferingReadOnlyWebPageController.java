package com.zzang.chongdae.offering.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class OfferingReadOnlyWebPageController {

    @GetMapping("/read-only/web/offerings/{offering-id}")
    String renderOfferingDetail(@PathVariable("offering-id") Long offeringId) {
        return "detail";
    }
}
