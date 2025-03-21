package com.zzang.chongdae.offering.controller;

import com.zzang.chongdae.offering.service.OfferingService;
import com.zzang.chongdae.offering.service.dto.OfferingMetaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class OfferingReadOnlyWebPageController {

    private final OfferingService offeringService;

    @Value("${storage.resourceHost}")
    private String resourceHost;

    @GetMapping("/read-only/web/offerings/{offering-id}")
    String renderOfferingDetail(@PathVariable("offering-id") Long offeringId, Model model) {
        OfferingMetaResponse response = offeringService.getOfferingMetaInfo(offeringId);
        model.addAttribute("offeringTitle", response.title());
        model.addAttribute("thumbnailUrl", response.thumbnailUrl());
        model.addAttribute("resourceHost", resourceHost);
        return "detail";
    }

    @GetMapping("/read-only/web/offerings")
    String renderOffering(Model model) {
        model.addAttribute("resourceHost", resourceHost);
        return "index";
    }
}
