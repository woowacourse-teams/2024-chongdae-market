package com.zzang.chongdae.offering.controller;

import com.zzang.chongdae.offering.service.OfferingService;
import com.zzang.chongdae.offering.service.dto.OfferingMetaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class OfferingReadOnlyWebPageController {

    private final OfferingService offeringService;

    @GetMapping("/read-only/web/offerings/{offering-id}")
    String renderOfferingDetail(@PathVariable("offering-id") Long offeringId, Model model) {
        OfferingMetaResponse response = offeringService.getOfferingMetaInfo(offeringId);
        model.addAttribute("offeringTitle", response.title());
        model.addAttribute("thumbnailUrl", response.thumbnailUrl());
        return "detail";
    }

    @GetMapping("/read-only/web/offerings")
    String renderOffering() {
        return "index";
    }
}
